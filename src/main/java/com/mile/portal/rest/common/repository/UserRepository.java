package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.User;
import com.mile.portal.rest.user.model.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);
}
