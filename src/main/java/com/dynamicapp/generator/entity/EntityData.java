package com.dynamicapp.generator.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
public class EntityData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long recordId;

    @ManyToOne
    private EntityMetadata entity;

    @ManyToOne
    private FieldMetadata field;

    private String dataValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public EntityMetadata getEntity() {
        return entity;
    }

    public void setEntity(EntityMetadata entity) {
        this.entity = entity;
    }

    public FieldMetadata getField() {
        return field;
    }

    public void setField(FieldMetadata field) {
        this.field = field;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }
}
