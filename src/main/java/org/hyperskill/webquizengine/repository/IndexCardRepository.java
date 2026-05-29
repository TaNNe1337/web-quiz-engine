package org.hyperskill.webquizengine.repository;

import org.hyperskill.webquizengine.model.IndexCard;
import org.hyperskill.webquizengine.model.Quiz;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IndexCardRepository extends PagingAndSortingRepository<IndexCard, Long> {
		@Query(value = "SELECT * FROM index_card ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
		Optional<IndexCard> findRandom();
	}
