package com.mile.portal.rest.common.model.domain.board;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mile.portal.rest.mng.model.domain.Manager;
import com.mile.portal.rest.user.model.domain.Client;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("Q")
public class BoardQna extends Board {

    @Column(length = 5)
    private String qnaType;

    @Lob
    private String answerContent;

    @ManyToOne
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
