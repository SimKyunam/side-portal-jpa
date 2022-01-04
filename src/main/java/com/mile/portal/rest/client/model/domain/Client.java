package com.mile.portal.rest.client.model.domain;

import com.mile.portal.rest.common.model.domain.Account;
import com.mile.portal.rest.common.model.domain.board.BoardQna;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("C")
@EqualsAndHashCode(callSuper = true)
public class Client extends Account {

    @Column(length = 100)
    private String icisNo;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<BoardQna> boardFaqs = new ArrayList<>();
}
