package org.hyperskill.webquizengine.controller;

import org.hyperskill.webquizengine.dto.CompletionDto;
import org.hyperskill.webquizengine.dto.QuizDto;
import org.hyperskill.webquizengine.dto.ResultDto;
import org.hyperskill.webquizengine.model.Quiz;
import org.hyperskill.webquizengine.service.QuizService;
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
import java.util.Set;
import java.util.stream.Collectors;

import static org.hyperskill.webquizengine.util.Utils.checkAnswerOptions;
import static org.hyperskill.webquizengine.util.Utils.convertQuizEntityToDtoWithoutAnswer;
import static org.hyperskill.webquizengine.util.Utils.convertQuizEntityToDtoWithAnswer;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/quizzes")
public class QuizController {
    private final Logger logger = LoggerFactory.getLogger(QuizController.class);
    private final QuizService service;

    @Autowired
    public QuizController(QuizService service) {
        this.service = service;
    }

    @PostMapping(path = "/{id}/solve", produces = APPLICATION_JSON_VALUE)
    public ResultDto solveQuiz(@PathVariable long id,
                               @RequestBody Set<Integer> answer,
                               @Autowired Principal principal) {
        logger.info("Solving a quiz {} with answer {}", id, answer);
        return service.solve(id, answer, principal.getName()) ?
                ResultDto.success() : ResultDto.failure();
    }
    @PostMapping(path = "/batch", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public List<QuizDto> createQuizBatch(@Valid @RequestBody List<QuizDto> quizDtos,
                                    @Autowired Principal principal) {
        logger.info("User {} wants to create {} quizzes", principal.getName(), quizDtos.size());

        return quizDtos.stream()
                .peek(quizDto -> {
                    checkAnswerOptions(quizDto);
                    var id = service.create(quizDto, principal.getName());
                    quizDto.setId(id);
                })
                .collect(Collectors.toList());
    }
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public QuizDto createQuiz(@Valid @RequestBody QuizDto quizDto,
                              @Autowired Principal principal) {
        logger.info("User {} wants to create a quiz {}", principal.getName(), quizDto);
        checkAnswerOptions(quizDto);
        var id = service.create(quizDto, principal.getName());
        quizDto.setId(id);
        return quizDto;
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public QuizDto getQuiz(@PathVariable long id) {
        return convertQuizEntityToDtoWithoutAnswer(service.findById(id));
    }
    
    @GetMapping(path = "/random", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<QuizDto> getRandomQuiz(@RequestParam(defaultValue = "1") Integer number) {
        List<Quiz> quizzes = service.findRandom(number);
        return quizzes.stream().map(Utils::convertQuizEntityToDtoWithAnswer).collect(Collectors.toList());
	}
    
    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public QuizDto putQuiz(@PathVariable long id,
							  @Valid @RequestBody QuizDto quizDto,
							  @Autowired Principal principal) {
		logger.info("User {} wants to update a quiz with id {} to {}", principal.getName(), id, quizDto);
		checkAnswerOptions(quizDto);
		var quiz = service.put(id, quizDto, principal.getName());
		return convertQuizEntityToDtoWithAnswer(quiz);
	}

    @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteQuiz(@PathVariable long id,
                           @Autowired Principal principal) {
        logger.info("User {} wants to delete a quiz with id {}", principal.getName(), id);
        service.delete(id, principal.getName());
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Page<QuizDto> getQuizPage(Pageable pageable) {
        return service.findAllAsPage(pageable)
                .map(Utils::convertQuizEntityToDtoWithoutAnswer);
    }

    @GetMapping(path = "/completed", produces = APPLICATION_JSON_VALUE)
    public Page<CompletionDto> getCompletedQuizPage(Principal principal, Pageable pageable) {
        return service.findAllCompletedQuizzesAsPage(principal.getName(), pageable)
                .map(Utils::convertCompletionEntityToDto);
    }
}
