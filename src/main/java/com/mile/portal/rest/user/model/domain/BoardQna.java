package com.mile.portal.rest.user.model.domain;

import com.mile.portal.rest.common.model.domain.BaseEntity;
import com.mile.portal.rest.mng.model.domain.Manager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted is null")
public class BoardQna extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_qna_id")
    private Long id;

    @Column(length = 5)
    private String qnaType;

    @Column(length = 50)
    private String qstTitle;

    @Lob
    private String qstContent;

    @Lob
    private String answerContent;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Manager manager;
}
