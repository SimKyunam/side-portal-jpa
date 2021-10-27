package com.mile.portal.rest.mng.repository;

import com.mile.portal.rest.mng.model.domain.Manager;
import com.mile.portal.rest.user.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByLoginId(String loginId);
}
