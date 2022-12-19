package com.agilesekeri.asugar_api.appuser;

import com.agilesekeri.asugar_api.project.Project;
import com.agilesekeri.asugar_api.project.ProjectService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.agilesekeri.asugar_api.security.authentication.CustomAuthenticationFilter.accessSecret;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Service
@AllArgsConstructor
@Transactional
public class AppUserService implements UserDetailsService {

    private final ProjectService projectService;

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

    public List<Project> getProjectList(Long userId) {
        AppUserEntity user = appUserRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("There are no projects were found for the user"));
        return projectService.getUserProjects(user);
    }

    public String getJWTUsername(HttpServletRequest request) throws IOException {

        String token = request.getHeader(AUTHORIZATION).substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256(accessSecret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }
}
