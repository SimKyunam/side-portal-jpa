package com.mile.portal.rest.common.model.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class Code {

    @Id
    @Column(name = "code_id")
    private String code;

    private String codeName;

    private String codeValue;

    private int ord;

    private int depth;

    @ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Code parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private final List<Code> child = new ArrayList<>();
}
