package org.zerock.jsontest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.jsontest.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByIdAndPassword(String id, String password);
}

