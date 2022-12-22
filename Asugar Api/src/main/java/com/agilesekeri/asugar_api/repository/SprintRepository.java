package com.agilesekeri.asugar_api.repository;

import com.agilesekeri.asugar_api.model.entity.SprintEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SprintRepository
        extends JpaRepository<SprintEntity, Long> {
    @Override
    Optional<SprintEntity> findById(Long sprintId);
}