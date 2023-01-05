package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.model.request.RegistrationRequest;
import com.agilesekeri.asugar_api.service.RegistrationService;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(path = "registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public void register(@RequestBody RegistrationRequest request, HttpServletResponse response) {
        var result = registrationService.register(request);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(result.getSecond());
        try {
            new ObjectMapper().writeValue(response.getOutputStream(), result.getFirst());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_GATEWAY.value());
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "confirm")
    public void confirm(@RequestParam("token") String token, HttpServletResponse response) {
        var result = registrationService.confirmToken(token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(result.getSecond().value());
        try {
            new ObjectMapper().writeValue(response.getOutputStream(), result.getFirst());
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_GATEWAY.value());
            throw new RuntimeException(e);
        }
    }
}
