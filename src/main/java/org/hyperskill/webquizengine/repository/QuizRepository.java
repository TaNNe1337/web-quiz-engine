package org.hyperskill.webquizengine.repository;

import java.util.Optional;

import org.hyperskill.webquizengine.model.Quiz;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends PagingAndSortingRepository<Quiz, Long> {
		@Query(value = "SELECT * FROM quiz ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
		Optional<Quiz> findRandom();
	}
