package com.mile.portal.rest.mng.model.domain;

import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.domain.board.Board;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("A")
@EqualsAndHashCode(callSuper = true)
public class Manager extends Account {

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<ManagerQna> managerQnas = new ArrayList<>();
}
