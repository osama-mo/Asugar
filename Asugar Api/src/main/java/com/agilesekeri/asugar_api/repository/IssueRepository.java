package com.agilesekeri.asugar_api.repository;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import com.agilesekeri.asugar_api.model.entity.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface IssueRepository
        extends JpaRepository<AbstractIssue, Long> {
    @Override
    Optional<AbstractIssue> findById(Long id);
}
