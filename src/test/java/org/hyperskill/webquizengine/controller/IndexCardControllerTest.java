package org.hyperskill.webquizengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperskill.webquizengine.dto.IndexCardDto;
import org.hyperskill.webquizengine.exception.IndexCardNotFoundException;
import org.hyperskill.webquizengine.model.IndexCard;
import org.hyperskill.webquizengine.service.IndexCardService;
import org.hyperskill.webquizengine.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.hyperskill.webquizengine.testutils.TestUtils.*;
import static org.hyperskill.webquizengine.testutils.TestUtils.DEFAULT_PASSWORD;
import static org.hyperskill.webquizengine.util.Utils.convertIndexCardDtoToEntity;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IndexCardController.class)
public class IndexCardControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private IndexCardService indexCardService;

    @MockBean
    private UserService service;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void testCreateIndexCard_whenUnauthorized() throws Exception {
        var indexCardWithId = createIndexCardWithId(1L);
        var indexCardWithoutId = createIndexCardWithoutId();

        when(indexCardService.create(any(), anyString())).thenReturn(indexCardWithId.getId());

        mvc.perform(post("/indexcards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(indexCardWithoutId)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = DEFAULT_USERNAME, password = DEFAULT_PASSWORD)
    public void testCreateIndexCard_whenNoAnswer() throws Exception {
        var indexCardWithId = createIndexCardWithId(1L);
        var indexCardWithoutId = createIndexCardWithoutId();

        indexCardWithoutId.setAnswer("");

        when(indexCardService.create(any(), anyString())).thenReturn(indexCardWithId.getId());

        mvc.perform(post("/indexcards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(indexCardWithoutId)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetIndexCard_whenExists() throws Exception {
        var indexCard = createIndexCardWithId(1L);

        when(indexCardService.findById(anyLong())).thenReturn(convertIndexCardDtoToEntity(indexCard));

        expectIndexCardJsonIsValid(mvc.perform(get(String.format("/indexcards/%s", indexCard.getId())))
                .andExpect(status().isOk()), indexCard);
    }
    @Test
    public void testGetIndexCard_whenIndexCardNotFound() throws Exception {
        when(indexCardService.findById(anyLong())).thenThrow(IndexCardNotFoundException.class);

        mvc.perform(get(String.format("/indexcards/%d", 1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetIndexCardList_evaluatesPageableParameter() throws Exception {
        var indexCards = createTestIndexCards(15);

        when(indexCardService.findAllAsPage(any())).thenReturn(new PageImpl<>(indexCards));

        mvc.perform(get("/indexcards")
                        .param("page", "5")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
    @Test
    @WithMockUser(username = DEFAULT_USERNAME, password = DEFAULT_PASSWORD)
    public void testDeleteIndexCard_whenSuccessful() throws Exception {
        mvc.perform(delete(String.format("/indexcards/%d", 1)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testDeleteIndexCard_whenUnauthorized() throws Exception {
        mvc.perform(delete(String.format("/indexcards/%d", 1)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = DEFAULT_USERNAME, password = DEFAULT_PASSWORD)
    public void testUpdateQuiz_whenSuccessful() throws Exception {
        long indexCardId = 1L;

        var updateIndexCard = new IndexCard();
        updateIndexCard.setTitle("Python Programming Basics");
        updateIndexCard.setText("What is the output of print(2 ** 3)?");
        updateIndexCard.setAnswer("16");

        var updatedIndexCard = new IndexCardDto();
        updatedIndexCard.setId(indexCardId);
        updatedIndexCard.setTitle("Advanced Python Concepts");
        updatedIndexCard.setText("What does enumerate() return?");
        updatedIndexCard.setAnswer("Tuple");

        when(indexCardService.put(eq(indexCardId), any(IndexCardDto.class), anyString()))
                .thenReturn(convertIndexCardDtoToEntity(updatedIndexCard));

        mvc.perform(put(String.format("/indexcards/%d", indexCardId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateIndexCard)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(indexCardId))
                .andExpect(jsonPath("$.title").value("Advanced Python Concepts"))
                .andExpect(jsonPath("$.text").value("What does enumerate() return?"))
                .andExpect(jsonPath("$.answer").value("Tuple"));
    }

}
