package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.model.entity.RegistrationTokenEntity;
import com.agilesekeri.asugar_api.repository.RegistrationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationTokenService {

    private final RegistrationTokenRepository registrationTokenRepository;

    public void saveConfirmationToken(RegistrationTokenEntity token) {
        registrationTokenRepository.save(token);
    }

    public Optional<RegistrationTokenEntity> getToken(String token) {
        return registrationTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return registrationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
