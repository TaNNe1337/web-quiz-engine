package org.hyperskill.webquizengine.service;

import org.hyperskill.webquizengine.dto.IndexCardDto;
import org.hyperskill.webquizengine.exception.NotPermittedException;
import org.hyperskill.webquizengine.exception.IndexCardNotFoundException;
import org.hyperskill.webquizengine.exception.UserNotFoundException;
import org.hyperskill.webquizengine.model.IndexCard;
import org.hyperskill.webquizengine.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

import java.util.Objects;

import static org.hyperskill.webquizengine.util.Utils.convertIndexCardDtoToEntity;
import static org.hyperskill.webquizengine.util.Utils.convertIndexCardEntityToDto;

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

    public IndexCard findById(long id) {
        return indexCardRepository.findById(id)
                .orElseThrow(IndexCardNotFoundException::new);
    }


    public IndexCardDto findRandom() {
        var indexCard = indexCardRepository.findRandom().orElseThrow(IndexCardNotFoundException::new);
        return convertIndexCardEntityToDto(indexCard);
    }

    public IndexCard put(long id, @Valid IndexCardDto indexCardDto, String name) {
        var indexCard = indexCardRepository.findById(id).orElseThrow(IndexCardNotFoundException::new);
        var user = userRepository.findByUsername(name).orElseThrow(UserNotFoundException::new);
        if(Objects.equals(indexCard.getCreatedBy().getId(), user.getId())) {
            indexCard = convertIndexCardDtoToEntity(indexCardDto);
            indexCard.setId(id);
            indexCard.setCreatedBy(user);
        }
        else {
            throw new NotPermittedException();
        }
        return indexCard;
    }

    public void delete(long id, String name) {
        var indexCard = indexCardRepository.findById(id).orElseThrow(IndexCardNotFoundException::new);
        var user = userRepository.findByUsername(name).orElseThrow(UserNotFoundException::new);
        if(Objects.equals(indexCard.getCreatedBy().getId(), user.getId())) {
            indexCardRepository.delete(indexCard);
        }
        else {
            throw new NotPermittedException();
        }
    }

    public Page<IndexCard> findAllAsPage(Pageable pageable) {
        return indexCardRepository.findAll(pageable);
    }
}
