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
@Table(name = "device")
public class Device {
    @Id
    @ColumnDefault("nextval('device_id_seq')")
    @Column(name = "device_id", nullable = false)
    private Integer id;

    @Column(name = "device_name", nullable = false)
    private String deviceName;

    @Column(name = "efuse_mac", nullable = false)
    private String efuseMac;

    @Column(name = "chip_model", nullable = false)
    private String chipModel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private DeviceType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "area_id")
    private Area area;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private Account createdBy;

    @OneToMany(mappedBy = "device", fetch = FetchType.EAGER)
    private Set<DeviceControl> deviceControls = new LinkedHashSet<>();

    @OneToMany(mappedBy = "device", fetch = FetchType.EAGER)
    private Set<DeviceEvent> deviceEvents = new LinkedHashSet<>();

    @OneToMany(mappedBy = "subscriber", fetch = FetchType.EAGER)
    private Set<Subscribe> subscribes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "device", fetch = FetchType.EAGER)
    private Set<DeviceService> deviceServices = new LinkedHashSet<>();
}