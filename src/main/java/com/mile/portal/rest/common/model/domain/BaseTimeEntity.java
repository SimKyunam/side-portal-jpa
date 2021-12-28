package com.mile.portal.rest.common.model.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime created;

    @LastModifiedDate
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updated;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime deleted;
}