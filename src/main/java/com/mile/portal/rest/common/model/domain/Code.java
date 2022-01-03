package com.mile.portal.rest.common.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mile.portal.rest.common.model.domain.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Where(clause = "deleted is null")
public class Code extends BaseEntity implements Serializable {

    @Id
    @Column(name = "code_id")
    private String code;

    @Column(nullable = false)
    private String codeName;

    private String codeValue;

    private int ord;

    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Code parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Code> child = new ArrayList<>();
}
