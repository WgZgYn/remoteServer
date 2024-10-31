package org.scu301.remoteserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "member")
public class Member {
    @Id
    @Column(name = "member_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;
}