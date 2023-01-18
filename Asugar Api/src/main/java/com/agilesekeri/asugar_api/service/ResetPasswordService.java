package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.service.AppUserService;
import com.agilesekeri.asugar_api.email.EmailSender;
import com.agilesekeri.asugar_api.model.entity.ResetPasswordTokenEntity;
import com.agilesekeri.asugar_api.service.ResetPasswordTokenService;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class ResetPasswordService {
    private final AppUserService appUserService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ResetPasswordTokenService resetPasswordTokenService;

    private final EmailSender emailSender;

    public Pair<String, Integer> changePasswordRequest(String email) {
        Pair<String, Integer> message;
        try {
            AppUserEntity appUser = appUserService.loadUserByUsername(email);

            if(!appUser.getEnabled())
                throw new IllegalStateException("The user is not enabled yet");

            String token = UUID.randomUUID().toString();
            ResetPasswordTokenEntity resetPasswordToken = new ResetPasswordTokenEntity(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    appUser
            );

            resetPasswordTokenService.saveResetPasswordToken(resetPasswordToken);

            sendVerificationEmail (
                    token,
                    email,
                    appUser.getFirstName()
            );

            message = Pair.of("An email is sent", HttpServletResponse.SC_ACCEPTED);
        } catch(UsernameNotFoundException e) {
            message = Pair.of(e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch(ServiceUnavailableException e) {
            message = Pair.of(e.getMessage(), HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        } catch(IllegalStateException e) {
            message = Pair.of(e.getMessage(), HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }

        return message;
    }

    @Transactional
    public Pair<String, Integer> confirmRequest(String token, String newPassword) {
        Pair<String, Integer> message;
        try {
            ResetPasswordTokenEntity resetPasswordToken = resetPasswordTokenService.getToken(token);
            if (resetPasswordToken.getConfirmedAt() != null)
                throw new IllegalStateException("request already confirmed");

            LocalDateTime expiredAt = resetPasswordToken.getExpiresAt();
            if (expiredAt.isBefore(LocalDateTime.now())) {
                String newToken = UUID.randomUUID().toString();
                ResetPasswordTokenEntity newResetPasswordToken = new ResetPasswordTokenEntity(
                        newToken,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        resetPasswordToken.getAppUser()
                );

                resetPasswordTokenService.saveResetPasswordToken(newResetPasswordToken);
                sendVerificationEmail (
                        newToken,
                        resetPasswordToken.getAppUser().getEmail(),
                        resetPasswordToken.getAppUser().getFirstName()
                );

                message = Pair.of("token expired, a new one is sent", HttpServletResponse.SC_ACCEPTED);
            }

            else {
                boolean uppercase = false, lowercase = false, integer = false;
                for (int i = 0; i < newPassword.length() && !(uppercase && lowercase && integer); ++i) {
                    char c = newPassword.charAt(i);
                    if (c >= 65 && c <= 90)
                        uppercase = true;
                    else if (c >= 97 && c <= 122)
                        lowercase = true;
                    else if (c >= 48 && c <= 57)
                        integer = true;
                }

                if(!(uppercase && lowercase && integer) || newPassword.length() < 8)
                    throw new IllegalArgumentException("Password is not valid.");

                else {
                    String encrypted = bCryptPasswordEncoder.encode(newPassword);
                    appUserService.changePassword(resetPasswordToken.getAppUser().getEmail(), encrypted);
                    resetPasswordTokenService.setConfirmedAt(token);
                        message = Pair.of("Password changed successfully", HttpServletResponse.SC_ACCEPTED);
                }
            }
        } catch(IllegalArgumentException|IllegalStateException e) {
            message = Pair.of(e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch(ServiceUnavailableException e) {
            message = Pair.of(e.getMessage(), HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

        return message;
    }

    public void sendVerificationEmail(String token, String email, String firstName)
            throws ServiceUnavailableException{
        try {
            String link = "http://localhost:8080/password_reset?token=";
            emailSender.send(
                    email,
                    buildEmail(firstName, token));
        } catch (Exception e) {
            throw new ServiceUnavailableException("email server not available");
        }
    }

    private String buildEmail(String name, String token) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Change Your Password</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Please use the code below to change your password: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + token + "</p></blockquote>\n Code will expire in 15 minutes." +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
