package com.mile.portal.rest.common.model.domain.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mile.portal.rest.client.model.domain.Client;
import com.mile.portal.rest.mng.model.domain.Manager;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("Q")
public class BoardQna extends Board {

    @Column(length = 10)
    @ColumnDefault("'Q_ETC'")
    private String qnaType;

    @Lob
    private String answerContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;

    @Builder
    public BoardQna(Long id, String title, String content, int readCnt, Manager manager, List<BoardAttach> boardAttachList,
                    String qnaType, String answerContent, Client client) {
        super(id, title, content, readCnt, manager, boardAttachList);

        this.qnaType = qnaType;
        this.answerContent = answerContent;
        this.client = client;
    }
}
