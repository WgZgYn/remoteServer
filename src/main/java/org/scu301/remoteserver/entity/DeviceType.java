package org.scu301.remoteserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "device_type")
public class DeviceType {
    @Id
    @ColumnDefault("nextval('device_type_type_id_seq')")
    @Column(name = "type_id", nullable = false)
    private Integer id;

    @Column(name = "type_name", nullable = false)
    private String type_name;
}