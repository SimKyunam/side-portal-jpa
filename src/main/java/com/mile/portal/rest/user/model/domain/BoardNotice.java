package com.mile.portal.rest.user.model.domain;

import com.mile.portal.rest.common.model.domain.BaseEntity;
import com.mile.portal.rest.mng.model.domain.Manager;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted is null")
public class BoardNotice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_notice_id")
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Manager manager;
}
