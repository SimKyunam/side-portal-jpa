package com.mile.portal.rest.common.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Code {

    @Id
    @Column(name = "code_id")
    private Long id;

    private String codeCd;

    private String codeName;

    private String codeValue;

    private int ord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Code parent;

    @OneToMany(mappedBy = "parent")
    private List<Code> child;
}
