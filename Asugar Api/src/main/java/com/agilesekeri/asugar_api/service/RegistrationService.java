package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.email.EmailSender;
import com.agilesekeri.asugar_api.email.EmailValidator;
import com.agilesekeri.asugar_api.model.request.RegistrationRequest;
import com.agilesekeri.asugar_api.model.entity.RegistrationTokenEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NameAlreadyBoundException;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final RegistrationTokenService registrationTokenService;
    private final EmailSender emailSender;


    public Pair<String, Integer> register(RegistrationRequest request) {
        Pair<String, Integer> message;

        try {
            if (!emailValidator.test(request.getEmail()))
                throw new IllegalStateException("email not valid");

            AppUserEntity newUser = AppUserEntity.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .password(request.getPassword()).build();

            appUserService.signUpUser(newUser);
            String token = UUID.randomUUID().toString();
            RegistrationTokenEntity confirmationToken = new RegistrationTokenEntity(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(60),
                    newUser
            );

            registrationTokenService.saveConfirmationToken(confirmationToken);

            try {
                sendVerificationEmail(token, request.getEmail(), request.getFirstName());
                message = Pair.of("A confirmation email is sent", HttpServletResponse.SC_ACCEPTED);
            } catch (Exception e) {
                message = Pair.of("email server not available", HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            }
        } catch (NameAlreadyBoundException e) {
            if(!appUserService.loadUserByUsername(request.getEmail()).getEnabled())
                message = Pair.of("The given email is already registered but not confirmed, a new confirmation email is sent.", HttpServletResponse.SC_ACCEPTED);
            else
                message = Pair.of("The given email is already registered.", HttpServletResponse.SC_BAD_REQUEST);
        } catch (IllegalStateException e) {
            message = Pair.of(e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }

        return message;
    }

    public void sendVerificationEmail(String token, String email, String firstName)
        throws IllegalStateException{
        try {
            String link = "https://primary:k9CixX1T7VPZ6TuHsPSSgk3Yv5PNWx8YLH4vKmZTNeU2hXMrzd8sNj5dgJwjNnaX@asugarappservice.test.azuremicroservices.io/asugarapi/default/registration/confirm?token=";
            emailSender.send(
                    email,
                    buildEmail(firstName, link + token));
        } catch (Exception e) {
            throw new IllegalStateException("email server not available");
        }
    }

    @Transactional
    public Pair<String, HttpStatus> confirmToken(String token) {
        RegistrationTokenEntity registrationToken = registrationTokenService
                .getToken(token).orElse(null);

        if(registrationToken == null)
            return Pair.of("Token not found", HttpStatus.NOT_FOUND);

        if (registrationToken.getConfirmedAt() != null) {
            return Pair.of("email already confirmed", HttpStatus.ALREADY_REPORTED);
        }

        LocalDateTime expiredAt = registrationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            String newToken = UUID.randomUUID().toString();
            RegistrationTokenEntity newRegistrationToken = new RegistrationTokenEntity(
                    newToken,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    registrationToken.getAppUser()
            );

            registrationTokenService.saveConfirmationToken(newRegistrationToken);

            sendVerificationEmail (
                    newToken,
                    registrationToken.getAppUser().getEmail(),
                    registrationToken.getAppUser().getFirstName()
            );

            return Pair.of("token expired, a new one is sent.", HttpStatus.ACCEPTED);
        }

        registrationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                registrationToken.getAppUser().getEmail());

        return Pair.of("confirmed", HttpStatus.ACCEPTED);
    }

    private String buildEmail(String name, String link) {
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
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
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
