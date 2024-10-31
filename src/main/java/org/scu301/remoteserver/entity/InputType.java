package org.scu301.remoteserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "input_type")
public class InputType {
    @Id
    @ColumnDefault("nextval('input_plugin_plugin_id_seq')")
    @Column(name = "input_type_id", nullable = false)
    private Integer id;

    @Column(name = "input_type", nullable = false)
    private String inputType;

    @Column(name = "template")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> template;

}