package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.model.entity.SprintEntity;
import com.agilesekeri.asugar_api.repository.SprintRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class SprintService {
    private final SprintRepository sprintRepository;

    public SprintEntity getSprint(Long id) {
        return sprintRepository.findById(id)
                .orElseThrow( () ->
                        new IllegalArgumentException("No sprint with this id was found"));
    }
}