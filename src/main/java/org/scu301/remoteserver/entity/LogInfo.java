package org.scu301.remoteserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "log_info")
public class LogInfo {
    @Id
    @ColumnDefault("nextval('log_info_id_seq')")
    @Column(name = "log_id", nullable = false)
    private Integer id;

    @Column(name = "\"time\"", nullable = false)
    private Instant time;

    @Column(name = "action")
    private String action;

}