package com.agilesekeri.asugar_api.security.password;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agilesekeri.asugar_api.appuser.AppUser;
import com.agilesekeri.asugar_api.appuser.AppUserService;
import com.agilesekeri.asugar_api.email.EmailSender;
import com.agilesekeri.asugar_api.security.password.token.ResetPasswordToken;
import com.agilesekeri.asugar_api.security.password.token.ResetPasswordTokenService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ResetPasswordService.class, BCryptPasswordEncoder.class})
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ResetPasswordServiceTest {
    @MockBean
    private AppUserService appUserService;

    @MockBean
    private EmailSender emailSender;

    @Autowired
    private ResetPasswordService resetPasswordService;

    @MockBean
    private ResetPasswordTokenService resetPasswordTokenService;

    /**
     * Method under test: {@link ResetPasswordService#confirmRequest(String, String)}
     */
    @Test
    void testConfirmRequest() {
        AppUser appUser = new AppUser();
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setId(123L);
        appUser.setLastName("Doe");
        appUser.setPassword("iloveyou");
        appUser.setProjects(new ArrayList<>());

        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setAppUser(appUser);
        resetPasswordToken.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken.setId(123L);
        resetPasswordToken.setToken("ABC123");
        Optional<ResetPasswordToken> ofResult = Optional.of(resetPasswordToken);
        when(resetPasswordTokenService.getToken((String) any())).thenReturn(ofResult);
        assertThrows(IllegalStateException.class, () -> resetPasswordService.confirmRequest("ABC123", "iloveyou"));
        verify(resetPasswordTokenService).getToken((String) any());
    }

    /**
     * Method under test: {@link ResetPasswordService#confirmRequest(String, String)}
     */
    @Test
    void testConfirmRequest2() {
        AppUser appUser = new AppUser();
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setId(123L);
        appUser.setLastName("Doe");
        appUser.setPassword("iloveyou");
        appUser.setProjects(new ArrayList<>());

        AppUser appUser1 = new AppUser();
        appUser1.setEmail("jane.doe@example.org");
        appUser1.setEnabled(true);
        appUser1.setFirstName("Jane");
        appUser1.setId(123L);
        appUser1.setLastName("Doe");
        appUser1.setPassword("iloveyou");
        appUser1.setProjects(new ArrayList<>());
        ResetPasswordToken resetPasswordToken = mock(ResetPasswordToken.class);
        when(resetPasswordToken.getAppUser()).thenReturn(appUser1);
        when(resetPasswordToken.getConfirmedAt()).thenReturn(null);
        when(resetPasswordToken.getExpiresAt()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(resetPasswordToken).setConfirmedAt((LocalDateTime) any());
        doNothing().when(resetPasswordToken).setCreatedAt((LocalDateTime) any());
        doNothing().when(resetPasswordToken).setExpiresAt((LocalDateTime) any());
        doNothing().when(resetPasswordToken).setId((Long) any());
        doNothing().when(resetPasswordToken).setToken((String) any());
        doNothing().when(resetPasswordToken).setAppUser((AppUser) any());
        resetPasswordToken.setAppUser(appUser);
        resetPasswordToken.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken.setId(123L);
        resetPasswordToken.setToken("ABC123");
        Optional<ResetPasswordToken> ofResult = Optional.of(resetPasswordToken);
        doNothing().when(resetPasswordTokenService).saveResetPasswordToken((ResetPasswordToken) any());
        when(resetPasswordTokenService.getToken((String) any())).thenReturn(ofResult);
        doNothing().when(emailSender).send((String) any(), (String) any());
        assertEquals("token expired, a new one is sent.", resetPasswordService.confirmRequest("ABC123", "iloveyou"));
        verify(resetPasswordTokenService).getToken((String) any());
        verify(resetPasswordTokenService).saveResetPasswordToken((ResetPasswordToken) any());
        verify(resetPasswordToken, atLeast(1)).getAppUser();
        verify(resetPasswordToken).getConfirmedAt();
        verify(resetPasswordToken).getExpiresAt();
        verify(resetPasswordToken).setConfirmedAt((LocalDateTime) any());
        verify(resetPasswordToken).setCreatedAt((LocalDateTime) any());
        verify(resetPasswordToken).setExpiresAt((LocalDateTime) any());
        verify(resetPasswordToken).setId((Long) any());
        verify(resetPasswordToken).setToken((String) any());
        verify(resetPasswordToken).setAppUser((AppUser) any());
        verify(emailSender).send((String) any(), (String) any());
    }

    /**
     * Method under test: {@link ResetPasswordService#confirmRequest(String, String)}
     */
    @Test
    void testConfirmRequest3() {
        AppUser appUser = new AppUser();
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setId(123L);
        appUser.setLastName("Doe");
        appUser.setPassword("iloveyou");
        appUser.setProjects(new ArrayList<>());

        AppUser appUser1 = new AppUser();
        appUser1.setEmail("jane.doe@example.org");
        appUser1.setEnabled(true);
        appUser1.setFirstName("Jane");
        appUser1.setId(123L);
        appUser1.setLastName("Doe");
        appUser1.setPassword("iloveyou");
        appUser1.setProjects(new ArrayList<>());
        ResetPasswordToken resetPasswordToken = mock(ResetPasswordToken.class);
        when(resetPasswordToken.getAppUser()).thenReturn(appUser1);
        when(resetPasswordToken.getConfirmedAt()).thenReturn(null);
        when(resetPasswordToken.getExpiresAt()).thenReturn(LocalDateTime.of(1, 1, 1, 1, 1));
        doNothing().when(resetPasswordToken).setConfirmedAt((LocalDateTime) any());
        doNothing().when(resetPasswordToken).setCreatedAt((LocalDateTime) any());
        doNothing().when(resetPasswordToken).setExpiresAt((LocalDateTime) any());
        doNothing().when(resetPasswordToken).setId((Long) any());
        doNothing().when(resetPasswordToken).setToken((String) any());
        doNothing().when(resetPasswordToken).setAppUser((AppUser) any());
        resetPasswordToken.setAppUser(appUser);
        resetPasswordToken.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        resetPasswordToken.setId(123L);
        resetPasswordToken.setToken("ABC123");
        Optional<ResetPasswordToken> ofResult = Optional.of(resetPasswordToken);
        doNothing().when(resetPasswordTokenService).saveResetPasswordToken((ResetPasswordToken) any());
        when(resetPasswordTokenService.getToken((String) any())).thenReturn(ofResult);
        doThrow(new IllegalStateException()).when(emailSender).send((String) any(), (String) any());
        assertThrows(IllegalStateException.class, () -> resetPasswordService.confirmRequest("ABC123", "iloveyou"));
        verify(resetPasswordTokenService).getToken((String) any());
        verify(resetPasswordTokenService).saveResetPasswordToken((ResetPasswordToken) any());
        verify(resetPasswordToken, atLeast(1)).getAppUser();
        verify(resetPasswordToken).getConfirmedAt();
        verify(resetPasswordToken).getExpiresAt();
        verify(resetPasswordToken).setConfirmedAt((LocalDateTime) any());
        verify(resetPasswordToken).setCreatedAt((LocalDateTime) any());
        verify(resetPasswordToken).setExpiresAt((LocalDateTime) any());
        verify(resetPasswordToken).setId((Long) any());
        verify(resetPasswordToken).setToken((String) any());
        verify(resetPasswordToken).setAppUser((AppUser) any());
        verify(emailSender).send((String) any(), (String) any());
    }
}

