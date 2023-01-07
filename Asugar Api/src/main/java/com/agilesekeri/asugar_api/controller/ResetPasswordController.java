package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.service.ResetPasswordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(path = "password_reset")
@AllArgsConstructor
public class ResetPasswordController {
    private final ResetPasswordService resetPasswordService;

    @GetMapping
    public void requestChangePassword(@RequestParam("email") String email,
                                      HttpServletResponse response)
            throws IOException {
        var message = resetPasswordService.changePasswordRequest(email);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(message.getSecond());
        new ObjectMapper().writeValue(response.getOutputStream(), message.getFirst());
    }

    @PostMapping
    public void confirm(@RequestHeader("Token") String token,
                        @RequestHeader("Password") String newPassword,
                        HttpServletResponse response)
            throws IOException {
        var message =  resetPasswordService.confirmRequest(token, newPassword);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(message.getSecond());
        new ObjectMapper().writeValue(response.getOutputStream(), message.getFirst());
    }
}
