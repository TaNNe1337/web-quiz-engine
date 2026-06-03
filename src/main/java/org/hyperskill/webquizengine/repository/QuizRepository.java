package org.hyperskill.webquizengine.repository;

import java.util.List;
import java.util.Optional;

import org.hyperskill.webquizengine.model.Quiz;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long> {
		@Query(value = "SELECT * FROM quiz ORDER BY RANDOM() LIMIT :number", nativeQuery = true)
		List<Quiz> findRandom(@Param("number")Integer number);
	}
