package com.mile.portal.rest.common.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Menu extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(nullable = false)
    private String menuName;

    private String menuValue;

    private int ord;

    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Menu parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Menu> child = new ArrayList<>();
}
