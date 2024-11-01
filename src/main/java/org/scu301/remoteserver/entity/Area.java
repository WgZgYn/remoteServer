package org.scu301.remoteserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "area")
public class Area {
    @Id
    @ColumnDefault("nextval('area_area_id_seq')")
    @Column(name = "area_id", nullable = false)
    private Integer id;

    @Column(name = "area_name", nullable = false)
    private String areaName;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Account createdBy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "area", cascade = CascadeType.ALL)
    private List<Device> devices;
}