package com.mile.portal.rest.common.model.domain.board;

import com.mile.portal.rest.common.model.domain.Attach;
import com.mile.portal.rest.common.model.enums.Authority;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("B")
public class BoardAttach extends Attach {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
    private String boardType;

    @Builder
    public BoardAttach(Long id, String attachName, String attachDown, String attachUpload, String path, Long size,
                       Board board, String boardType) {
        super(id, attachName, attachDown, attachUpload, path, size);

        this.board = board;
        this.boardType = boardType;
    }

    public void setAttachParam(Attach attach) {
        super.setId(attach.getId());
        super.setAttachName(attach.getAttachName());
        super.setAttachDown(attach.getAttachDown());
        super.setAttachUpload(attach.getAttachUpload());
        super.setPath(attach.getPath());
        super.setSize(attach.getSize());
    }

}
