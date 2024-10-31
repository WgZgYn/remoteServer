package org.scu301.remoteserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "device_service")
public class DeviceService {
    @Id
    @Column(name = "device_id", nullable = false)
    private Integer id;

    @MapsId
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "parameters")
    private String parameters;

}