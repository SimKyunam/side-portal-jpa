package com.mile.portal.rest.user.model.domain;

import com.mile.portal.rest.common.model.domain.BaseEntity;
import com.mile.portal.rest.mng.model.domain.Manager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BoardNotice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5)
    private String ntcType;

    @Column(length = 50)
    private String title;

    @Lob
    private String content;

    @Column(columnDefinition = "int(11) default 0")
    private int readCnt;

    private LocalDateTime beginDate;

    private LocalDateTime endDate;

    @Column(length = 1)
    private String hotYn;

    @Column(length = 1)
    private String pubYn;

    @ManyToOne
    private Manager manager;
}
