package com.dynamicapp.generator.repository;

import com.dynamicapp.generator.entity.EntityMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface EntityMetadataRepository extends JpaRepository<EntityMetadata, Long> {
    Optional<EntityMetadata> findByName(String name);


}
