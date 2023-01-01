package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.repository.AppUserRepository;
import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.agilesekeri.asugar_api.security.authentication.CustomAuthenticationFilter.accessSecret;
import static com.agilesekeri.asugar_api.security.authentication.CustomAuthenticationFilter.refreshSecret;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;


@Service
@AllArgsConstructor
@Transactional
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG =
            "User with the email %s was not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public AppUserEntity loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public void signUpUser(AppUserEntity appUser) {
        if (userExists(appUser.getEmail()))
            throw new IllegalStateException("email already taken");

        String encodedPassword = bCryptPasswordEncoder
                .encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public int changePassword(String email, String newPass) {
        if (!userExists(email))
            throw new IllegalStateException("This user does not exist.");

        return appUserRepository.changePassword(email, newPass);
    }

    public boolean userExists(String email) {
        return appUserRepository
                .findByEmail(email).isPresent();
    }

    public String getJWTUsername(HttpServletRequest request) throws IOException {
        String token = request.getHeader(AUTHORIZATION);
        Algorithm algorithm = Algorithm.HMAC256(accessSecret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

    public void genRefreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refresh_token = request.getHeader(AUTHORIZATION);
        Map<String, String> responseMap = new HashMap<>();
        if(refresh_token != null) {
            try {
                Algorithm refreshAlgorithm = Algorithm.HMAC256(refreshSecret);
                JWTVerifier verifier = JWT.require(refreshAlgorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                loadUserByUsername(username);

                Algorithm accessAlgorithm = Algorithm.HMAC256(accessSecret);
                String access_token = JWT.create()
                        .withSubject(username)
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .sign(accessAlgorithm);

                responseMap.put("accessToken", access_token);
                responseMap.put("refreshToken", refresh_token);
            }catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                responseMap.put("error_message", exception.getMessage());
            }
        } else {
            throw new RuntimeException("Token is missing");
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseMap);
    }
}
