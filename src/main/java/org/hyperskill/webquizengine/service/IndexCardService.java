package org.hyperskill.webquizengine.service;

import org.hyperskill.webquizengine.dto.IndexCardDto;
import org.hyperskill.webquizengine.dto.QuizDto;
import org.hyperskill.webquizengine.exception.UserNotFoundException;
import org.hyperskill.webquizengine.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

import static org.hyperskill.webquizengine.util.Utils.convertIndexCardDtoToEntity;

@Service
public class IndexCardService {
    private final IndexCardRepository indexCardRepository;
    private final UserRepository userRepository;

    @Autowired
    public IndexCardService(IndexCardRepository indexCardRepository,
                       UserRepository userRepository) {
        this.userRepository = userRepository;
        this.indexCardRepository = indexCardRepository;
    }

    public Long create(IndexCardDto indexCardDto, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        var indexCard = convertIndexCardDtoToEntity(indexCardDto);
        indexCard.setCreatedBy(user);

        return indexCardRepository.save(indexCard).getId();
    }
}
