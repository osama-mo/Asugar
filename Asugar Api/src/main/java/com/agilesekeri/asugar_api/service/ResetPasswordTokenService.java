package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.model.entity.ResetPasswordTokenEntity;
import com.agilesekeri.asugar_api.repository.ResetPasswordTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ResetPasswordTokenService {

    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    public void saveResetPasswordToken(ResetPasswordTokenEntity token) {
        resetPasswordTokenRepository.save(token);
    }

    public Optional<ResetPasswordTokenEntity> getToken(String token) {
        return resetPasswordTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return resetPasswordTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
