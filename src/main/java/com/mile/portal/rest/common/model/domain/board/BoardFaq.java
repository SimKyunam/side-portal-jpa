package com.mile.portal.rest.common.model.domain.board;

import com.mile.portal.rest.mng.model.domain.Manager;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("F")
public class BoardFaq extends Board {

    @Column(length = 10)
    @ColumnDefault("'F_ETC'")
    private String faqType;

    @Builder
    public BoardFaq(Long id, String title, String content, int readCnt, Manager manager, List<BoardAttach> boardAttachList,
                    String faqType) {
        super(id, title, content, readCnt, manager, boardAttachList);
        this.faqType = faqType;
    }
}
