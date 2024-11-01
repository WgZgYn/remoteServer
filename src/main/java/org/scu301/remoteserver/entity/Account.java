package org.scu301.remoteserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq_gen")
    @SequenceGenerator(name = "account_id_seq_gen", sequenceName = "account_id_seq", allocationSize = 1)
    @Column(name = "account_id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "salt", nullable = false)
    private String salt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "last_login")
    private Instant lastLogin;

    @ColumnDefault("'active'")
    @Column(name = "status", length = 50)
    private String status;

    @ColumnDefault("'user'")
    @Column(name = "role", columnDefinition = "account_role not null")
    private String role;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Member> members = new LinkedHashSet<>();

//    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
//    private UserInfo userInfo;

//    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<Member> members = new ArrayList<>(); // 用于关联 Member
}