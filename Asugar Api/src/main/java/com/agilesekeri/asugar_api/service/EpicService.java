package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.model.entity.EpicEntity;
import com.agilesekeri.asugar_api.repository.EpicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EpicService {
    private final EpicRepository epicRepository;

    public EpicEntity getEpic(Long epicId) {
        return epicRepository.findById(epicId)
                .orElseThrow( () ->
                        new IllegalArgumentException("No epic with this ID was found"));
    }
}