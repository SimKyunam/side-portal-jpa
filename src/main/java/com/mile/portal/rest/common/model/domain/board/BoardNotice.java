package com.mile.portal.rest.common.model.domain.board;

import com.mile.portal.rest.common.model.domain.BaseEntity;
import com.mile.portal.rest.mng.model.domain.Manager;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("N")
@Accessors(chain = true)
public class BoardNotice extends Board {

    @Column(length = 5)
    private String ntcType;

    private LocalDateTime beginDate;

    private LocalDateTime endDate;

    @Column(length = 1)
    private String hotYn;

    @Column(length = 1)
    private String pubYn;

    @Builder
    public BoardNotice(Long id, String title, String content, int readCnt, Manager manager, List<BoardAttach> boardAttachList,
                       String ntcType, LocalDateTime beginDate, LocalDateTime endDate,
                       String hotYn, String pubYn) {
        super(id, title, content, readCnt, manager, boardAttachList);

        this.ntcType = ntcType;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.hotYn = hotYn;
        this.pubYn = pubYn;
    }
}
