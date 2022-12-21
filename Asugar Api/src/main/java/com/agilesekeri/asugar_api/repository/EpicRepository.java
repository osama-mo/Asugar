package com.agilesekeri.asugar_api.repository;

import com.agilesekeri.asugar_api.model.entity.EpicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface EpicRepository
        extends JpaRepository<EpicEntity, Long> {
    @Override
    Optional<EpicEntity> findById(Long id);
}
