package org.hyperskill.webquizengine.repository;

import java.util.List;

import org.hyperskill.webquizengine.model.Option;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends CrudRepository<Option, Long> {

    @Query("SELECT c FROM Option c where quiz_id = :quizid")
    List<Option> findAllByQuiz(Long quizid);
}
