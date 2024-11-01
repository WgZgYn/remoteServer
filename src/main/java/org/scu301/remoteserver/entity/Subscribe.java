package org.scu301.remoteserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "subscribe")
public class Subscribe {
    @Id
    @ColumnDefault("nextval('subscribe_subscribe_id_seq')")
    @Column(name = "subscribe_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "event", nullable = false)
    private DeviceEvent event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscriber", nullable = false)
    private Device subscriber;

}