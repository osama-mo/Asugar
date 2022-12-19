package com.agilesekeri.asugar_api.registration.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agilesekeri.asugar_api.appuser.AppUserEntity;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RegistrationTokenServiceTest {
    @MockBean
    private RegistrationTokenRepository registrationTokenRepository;

    @Autowired
    private RegistrationTokenService registrationTokenService;

    /**
     * Method under test: {@link RegistrationTokenService#saveConfirmationToken(RegistrationToken)}
     */
    @Test
    void testSaveConfirmationToken() {
        AppUserEntity appUser = new AppUserEntity();
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setId(123L);
        appUser.setLastName("Doe");
        appUser.setPassword("iloveyou");
        appUser.setProjects(new HashSet<>());

        RegistrationToken registrationToken = new RegistrationToken();
        registrationToken.setAppUser(appUser);
        registrationToken.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        registrationToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        registrationToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        registrationToken.setId(123L);
        registrationToken.setToken("ABC123");
        when(registrationTokenRepository.save((RegistrationToken) any())).thenReturn(registrationToken);

        AppUserEntity appUser1 = new AppUserEntity();
        appUser1.setEmail("jane.doe@example.org");
        appUser1.setEnabled(true);
        appUser1.setFirstName("Jane");
        appUser1.setId(123L);
        appUser1.setLastName("Doe");
        appUser1.setPassword("iloveyou");
        appUser1.setProjects(new HashSet<>());

        RegistrationToken registrationToken1 = new RegistrationToken();
        registrationToken1.setAppUser(appUser1);
        registrationToken1.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        registrationToken1.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        registrationToken1.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        registrationToken1.setId(123L);
        registrationToken1.setToken("ABC123");
        registrationTokenService.saveConfirmationToken(registrationToken1);
        verify(registrationTokenRepository).save((RegistrationToken) any());
        assertEquals(appUser, registrationToken1.getAppUser());
        assertEquals("ABC123", registrationToken1.getToken());
        assertEquals(123L, registrationToken1.getId().longValue());
        assertEquals("0001-01-01", registrationToken1.getConfirmedAt().toLocalDate().toString());
        assertEquals("0001-01-01", registrationToken1.getExpiresAt().toLocalDate().toString());
        assertEquals("0001-01-01", registrationToken1.getCreatedAt().toLocalDate().toString());
    }

    /**
     * Method under test: {@link RegistrationTokenService#getToken(String)}
     */
    @Test
    void testGetToken() {
        assertFalse(registrationTokenService.getToken("ABC123").isPresent());
    }

    /**
     * Method under test: {@link RegistrationTokenService#setConfirmedAt(String)}
     */
    @Test
    void testSetConfirmedAt() {
        assertEquals(0, registrationTokenService.setConfirmedAt("ABC123"));
        assertEquals(0, registrationTokenService.setConfirmedAt("select distinct U from"));
    }
}

