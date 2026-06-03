package org.hyperskill.webquizengine.controller;

import org.hyperskill.webquizengine.dto.IndexCardDto;
import org.hyperskill.webquizengine.service.IndexCardService;
import org.hyperskill.webquizengine.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.hyperskill.webquizengine.util.Utils.*;
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

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public IndexCardDto getIndexCardById(@PathVariable long id) {
        logger.info("User wants to get Index card with id {}", id);
        return convertIndexCardEntityToDto(service.findById(id));
    }

    @GetMapping(path = "/random", produces = APPLICATION_JSON_VALUE)
    public IndexCardDto getRandomQuiz() {
        return service.findRandom();
    }

    @PostMapping(path = "/batch", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public List<IndexCardDto> createQuizBatch(@Valid @RequestBody List<IndexCardDto> indexCardDtos,
                                         @Autowired Principal principal) {
        logger.info("User {} wants to create {} quizzes", principal.getName(), indexCardDtos.size());

        return indexCardDtos.stream()
                .peek(indexCardDto -> {
                    var id = service.create(indexCardDto, principal.getName());
                    indexCardDto.setId(id);
                })
                .collect(Collectors.toList());
    }

    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public IndexCardDto updateIndexCard(@PathVariable long id,
                                   @Valid @RequestBody IndexCardDto indexCardDto,
                                   @Autowired Principal principal) {
        logger.info("User {} wants to update a quiz with id {} to {}", principal.getName(), id, indexCardDto);
        var indexCard = service.put(id, indexCardDto, principal.getName());
        return convertIndexCardEntityToDto(indexCard);
    }

    @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteIndexCard(@PathVariable long id,
                           @Autowired Principal principal) {
        logger.info("User {} wants to delete a index card with id {}", principal.getName(), id);
        service.delete(id, principal.getName());
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Page<IndexCardDto> getIndexCardPage(Pageable pageable) {
        return service.findAllAsPage(pageable)
                .map(Utils::convertIndexCardEntityToDto);
    }

}
