package org.hyperskill.webquizengine.controller;

import org.hyperskill.webquizengine.dto.IndexCardDto;
import org.hyperskill.webquizengine.dto.QuizDto;
import org.hyperskill.webquizengine.service.IndexCardService;
import org.hyperskill.webquizengine.service.QuizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static org.hyperskill.webquizengine.util.Utils.checkAnswerOptions;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/indexcards")
public class IndexCardController {
    private final Logger logger = LoggerFactory.getLogger(IndexCardController.class);
    private final IndexCardService service;

    @Autowired
    public IndexCardController(IndexCardService service) {
        this.service = service;
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public IndexCardDto createIndexCard(@Valid @RequestBody IndexCardDto indexCardDto,
                                   @Autowired Principal principal) {
        logger.info("User {} wants to create a Index card {}", principal.getName(), indexCardDto);
        var id = service.create(indexCardDto, principal.getName());
        indexCardDto.setId(id);
        return indexCardDto;
    }
}
