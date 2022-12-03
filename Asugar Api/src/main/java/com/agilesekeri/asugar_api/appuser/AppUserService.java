package com.agilesekeri.asugar_api.appuser;

import com.agilesekeri.asugar_api.project.Project;
import com.agilesekeri.asugar_api.project.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
    public AppUser loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public void signUpUser(AppUser appUser) {
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
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("There are no projects were found for the user"));
        return projectService.getUserProjects(user);
    }
}
