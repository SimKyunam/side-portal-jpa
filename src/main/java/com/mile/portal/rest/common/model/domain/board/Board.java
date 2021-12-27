package com.mile.portal.rest.common.model.domain.board;

import com.mile.portal.rest.common.model.domain.BaseEntity;
import com.mile.portal.rest.mng.model.domain.Manager;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "board_type")
@Where(clause = "deleted is null")
@Accessors(chain = true)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(length = 50)
    private String title;

    @Lob
    private String content;

    @ColumnDefault(value = "0")
    private int readCnt;

    @ManyToOne
    private Manager manager;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board")
    private List<BoardAttach> boardAttachList = new ArrayList<>();
}
