package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);
}
