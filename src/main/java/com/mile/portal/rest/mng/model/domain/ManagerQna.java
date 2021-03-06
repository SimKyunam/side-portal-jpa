package com.mile.portal.rest.mng.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mile.portal.rest.common.model.domain.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted is null")
public class ManagerQna extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_qna_id")
    private Long id;

    @Column(length = 10)
    private String qnaType;

    @Column(length = 1)
    private String mailSendYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    @JsonIgnore
    private Manager manager;
}
