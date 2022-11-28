package com.agilesekeri.asugar_api.security.password;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "password_reset")
@AllArgsConstructor
public class ResetPasswordController {
    private final ResetPasswordService resetPasswordService;

    @GetMapping
    public String requestChangePassword(@RequestParam("email") String email) {
        return resetPasswordService.changePasswordRequest(email);
    }

    @PostMapping
    public String confirm(@RequestHeader("Token") String token,
                          @RequestHeader("Password") String newPassword) {
        return resetPasswordService.confirmRequest(token, newPassword);
    }
}
