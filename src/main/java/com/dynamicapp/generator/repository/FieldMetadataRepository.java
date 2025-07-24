package com.dynamicapp.generator.repository;

import com.dynamicapp.generator.entity.EntityMetadata;
import com.dynamicapp.generator.entity.FieldMetadata;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldMetadataRepository extends JpaRepository<FieldMetadata, Long> {
    List<FieldMetadata> findByEntity(EntityMetadata entity);
    @Transactional
    void deleteByEntity_Id(long id);
}
