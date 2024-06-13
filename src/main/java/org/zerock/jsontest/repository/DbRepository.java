package org.zerock.jsontest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.jsontest.domain.Travel;

import java.util.Optional;

public interface DbRepository extends JpaRepository<Travel, String> {
    Optional<Travel> findFirstByTitleContainingIgnoreCase(String title);
}
