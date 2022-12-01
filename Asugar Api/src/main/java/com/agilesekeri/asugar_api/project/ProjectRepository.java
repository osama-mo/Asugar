package com.agilesekeri.asugar_api.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ProjectRepository
    extends JpaRepository<Project, Long>
{
    @Override
    Optional<Project> findById(Long id);

    Optional<List<Project>> findByMembers_Id(Long member);

//    Optional<Project> findByAdminId(Long adminId, String name);
}
