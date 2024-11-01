package org.scu301.remoteserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "device_control")
public class DeviceControl {
    @Id
    @ColumnDefault("nextval('device_control_device_control_id_seq')")
    @Column(name = "device_control_id", nullable = false)
    private Integer id;

    @Column(name = "label", nullable = false)
    private String label;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "input_type_id", nullable = false)
    private InputType inputType;

    @Column(name = "parameter")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> parameter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;
}