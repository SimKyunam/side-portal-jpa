package com.mile.portal.rest.common.model.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Code {

    @Id
    @Column(name = "code_id")
    private Long id;

    private String codeCd;

    private String codeName;

    private String codeValue;

    private int ord;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Code parent;

    @OneToMany(mappedBy = "parent")
    @ToString.Exclude
    private List<Code> child = new ArrayList<>();
}
