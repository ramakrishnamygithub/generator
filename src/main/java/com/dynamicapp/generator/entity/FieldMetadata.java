package com.dynamicapp.generator.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
public class FieldMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private EntityMetadata entity;

    private String fieldName;
    private String fieldType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityMetadata getEntity() {
        return entity;
    }

    public void setEntity(EntityMetadata entity) {
        this.entity = entity;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}
