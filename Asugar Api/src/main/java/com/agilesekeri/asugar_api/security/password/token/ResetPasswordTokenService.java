package com.agilesekeri.asugar_api.security.password.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ResetPasswordTokenService {

    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    public void saveResetPasswordToken(ResetPasswordToken token) {
        resetPasswordTokenRepository.save(token);
    }

    public Optional<ResetPasswordToken> getToken(String token) {
        return resetPasswordTokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return resetPasswordTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
