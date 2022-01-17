package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByLoginId(String loginId);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByLoginIdAndEmailCheckYn(String loginId, String emailCheckYn);

    boolean existsByLoginId(String loginId);

    boolean existsByEmail(String email);

    Optional<Account> findByLoginIdAndEmailCode(String loginId, String emailCode);
}
