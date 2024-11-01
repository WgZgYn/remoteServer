package org.scu301.remoteserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "device_event")
public class DeviceEvent {
    @Id
    @ColumnDefault("nextval('event_event_id_seq')")
    @Column(name = "event_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;

    @Column(name = "event_message")
    private String eventMessage;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<Subscribe> subscribes = new LinkedHashSet<>();

}