package org.hyperskill.webquizengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperskill.webquizengine.service.IndexCardService;
import org.hyperskill.webquizengine.service.QuizService;
import org.hyperskill.webquizengine.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hyperskill.webquizengine.testutils.TestUtils.*;
import static org.hyperskill.webquizengine.testutils.TestUtils.DEFAULT_PASSWORD;
import static org.hyperskill.webquizengine.testutils.TestUtils.createJavaLogoQuizWithId;
import static org.hyperskill.webquizengine.testutils.TestUtils.createJavaLogoQuizWithoutId;
import static org.hyperskill.webquizengine.testutils.TestUtils.expectQuizJsonIsValid;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
