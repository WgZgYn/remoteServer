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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_service_id_gen")
    @SequenceGenerator(name = "device_service_id_gen", sequenceName = "device_service_device_service_id_seq", allocationSize = 1)
    @Column(name = "device_service_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "parameters")
    private String parameters;

}