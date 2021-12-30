package com.mile.portal.rest.common.repository;

import com.mile.portal.rest.common.model.domain.Attach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachRepository extends JpaRepository<Attach, Long> {

    Optional<Attach> findByAttachUpload(String fileName);
}
