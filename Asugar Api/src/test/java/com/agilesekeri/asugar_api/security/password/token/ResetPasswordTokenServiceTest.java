package com.agilesekeri.asugar_api.security.password.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;

import java.time.LocalDateTime;
import java.util.HashSet;

import com.agilesekeri.asugar_api.model.entity.ResetPasswordTokenEntity;
import com.agilesekeri.asugar_api.repository.ResetPasswordTokenRepository;
import com.agilesekeri.asugar_api.service.ResetPasswordTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ResetPasswordTokenServiceTest {
    @MockBean
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Autowired
    private ResetPasswordTokenService resetPasswordTokenService;

    /**
     * Method under test: {@link ResetPasswordTokenService#saveResetPasswordToken(ResetPasswordTokenEntity)}
     */
    @Test
    void testSaveResetPasswordToken() {
        AppUserEntity appUser = new AppUserEntity();
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setId(123L);
        appUser.setLastName("Doe");
        appUser.setPassword("iloveyou");
        appUser.setProjects(new HashSet<>());

        ResetPasswordTokenEntity resetPasswordToken = new ResetPasswordTokenEntity();
        resetPasswordToken.setAppUser(appUser);
        resetPasswordToken.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken.setId(123L);
        resetPasswordToken.setToken("ABC123");
        when(resetPasswordTokenRepository.save((ResetPasswordTokenEntity) any())).thenReturn(resetPasswordToken);

        AppUserEntity appUser1 = new AppUserEntity();
        appUser1.setEmail("jane.doe@example.org");
        appUser1.setEnabled(true);
        appUser1.setFirstName("Jane");
        appUser1.setId(123L);
        appUser1.setLastName("Doe");
        appUser1.setPassword("iloveyou");
        appUser1.setProjects(new HashSet<>());

        ResetPasswordTokenEntity resetPasswordToken1 = new ResetPasswordTokenEntity();
        resetPasswordToken1.setAppUser(appUser1);
        resetPasswordToken1.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken1.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken1.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken1.setId(123L);
        resetPasswordToken1.setToken("ABC123");
        resetPasswordTokenService.saveResetPasswordToken(resetPasswordToken1);
        verify(resetPasswordTokenRepository).save((ResetPasswordTokenEntity) any());
        assertEquals(appUser, resetPasswordToken1.getAppUser());
        assertEquals("ABC123", resetPasswordToken1.getToken());
        assertEquals(123L, resetPasswordToken1.getId().longValue());
        assertEquals("0001-01-01", resetPasswordToken1.getConfirmedAt().toLocalDate().toString());
        assertEquals("0001-01-01", resetPasswordToken1.getExpiresAt().toLocalDate().toString());
        assertEquals("0001-01-01", resetPasswordToken1.getCreatedAt().toLocalDate().toString());
    }

    /**
     * Method under test: {@link ResetPasswordTokenService#getToken(String)}
     */
    @Test
    void testGetToken() {
        assertFalse(resetPasswordTokenService.getToken("ABC123").isPresent());
    }

    /**
     * Method under test: {@link ResetPasswordTokenService#setConfirmedAt(String)}
     */
    @Test
    void testSetConfirmedAt() {
        assertEquals(0, resetPasswordTokenService.setConfirmedAt("ABC123"));
        assertEquals(0, resetPasswordTokenService.setConfirmedAt("select distinct U from"));
    }
}

