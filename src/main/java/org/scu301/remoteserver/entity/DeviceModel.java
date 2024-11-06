package org.scu301.remoteserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "device_model")
public class DeviceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_model_id_gen")
    @SequenceGenerator(name = "device_model_id_gen", sequenceName = "device_model_model_id_seq", allocationSize = 1)
    @Column(name = "model_id", nullable = false)
    private Integer id;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private DeviceType type;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ColumnDefault("'0.0.1'")
    @Column(name = "version", length = 32)
    private String version;

    @OneToMany(mappedBy = "model")
    private List<DeviceControl> deviceControls = new ArrayList<>();
}