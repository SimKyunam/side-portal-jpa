package com.mile.portal.rest.user.repository;

import com.mile.portal.rest.user.model.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByLoginId(String loginId);
}
