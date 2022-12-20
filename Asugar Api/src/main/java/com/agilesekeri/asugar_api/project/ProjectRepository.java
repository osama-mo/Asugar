package com.agilesekeri.asugar_api.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ProjectRepository
    extends JpaRepository<ProjectEntity, Long>
{
    @Override
    Optional<ProjectEntity> findById(Long id);

    Optional<List<ProjectEntity>> findByMembers_Id(Long member);
}
