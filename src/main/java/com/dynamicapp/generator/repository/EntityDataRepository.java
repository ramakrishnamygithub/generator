package com.dynamicapp.generator.repository;

import com.dynamicapp.generator.entity.EntityData;
import com.dynamicapp.generator.entity.EntityMetadata;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntityDataRepository  extends JpaRepository<EntityData, Long> {
    List<EntityData> findByEntity(EntityMetadata entity);
    @Transactional
    void deleteByEntity(EntityMetadata entity);
}
