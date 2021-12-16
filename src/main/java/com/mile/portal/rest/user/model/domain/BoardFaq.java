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
public class BoardFaq extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_faq_id")
    private Long id;

    @Column(length = 5)
    private String faqType;

    @Column(length = 50)
    private String title;

    @Lob
    private String content;

    @Column(columnDefinition = "int(11) default 0")
    private int readCnt;

    @ManyToOne
    private Manager manager;
}
