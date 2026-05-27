package org.hyperskill.webquizengine.controller;

import org.hyperskill.webquizengine.dto.UsersDto;
import org.hyperskill.webquizengine.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@CrossOrigin(origins = {"https://htw-dresden.de", "https://informatik.htw-dresden.de"})
@RestController
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService service;

    @Autowired
    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping(path = "/register", consumes = APPLICATION_JSON_VALUE)
    public void register(@Valid @RequestBody UsersDto userDto) {
        logger.info("A new user '{}' wants to register", userDto.getEmail());
        service.registerNewUser(userDto.getEmail(), userDto.getPassword());
    }
}
