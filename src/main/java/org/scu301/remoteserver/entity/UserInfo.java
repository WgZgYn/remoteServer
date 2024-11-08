package org.scu301.remoteserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @Column(name = "account_id", nullable = false)
    private Integer id;

//    @MapsId
//    @OneToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "account_id", nullable = false)
//    private Account account;

    @Column(name = "gender", length = Integer.MAX_VALUE)
    private String gender;

    @Column(name = "location")
    private String location;

    @Column(name = "age")
    private Integer age;

    @Column(name = "email")
    private String email;
}