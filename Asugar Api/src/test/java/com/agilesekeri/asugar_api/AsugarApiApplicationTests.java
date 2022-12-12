package com.agilesekeri.asugar_api;

import com.agilesekeri.asugar_api.appuser.AppUser;
import com.agilesekeri.asugar_api.appuser.AppUserController;
import com.agilesekeri.asugar_api.appuser.AppUserRepository;
import com.agilesekeri.asugar_api.appuser.AppUserService;
import com.agilesekeri.asugar_api.email.EmailValidator;
import com.agilesekeri.asugar_api.project.Project;
import com.agilesekeri.asugar_api.project.ProjectController;
import com.agilesekeri.asugar_api.project.ProjectRepository;
import com.agilesekeri.asugar_api.project.ProjectService;
import com.agilesekeri.asugar_api.registration.RegistrationController;
import com.agilesekeri.asugar_api.registration.RegistrationRequest;
import com.agilesekeri.asugar_api.registration.RegistrationService;
import org.apache.catalina.LifecycleState;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AsugarApiApplicationTests {
    @Autowired
    private AppUserService appUserService;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private RegistrationController registrationController;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectController projectController;

    @Test
    @Order(1)
    void contextLoads() {

    }

    @Test
    @Order(2)
    void testRegister() {
        RegistrationRequest request = new RegistrationRequest("Enes", "Onur", "email@test.com", "pass");
        assertEquals("A confirmation email is sent" , registrationController.register(request));

        RegistrationRequest request2 = new RegistrationRequest("Enes", "Onur", "@test.com", "pass");
        assertThrows(IllegalStateException.class , () -> registrationController.register(request2));

        RegistrationRequest request3 = new RegistrationRequest("Enes", "Onur", "email@test.com", "pass");
        assertEquals("The given email is already registered but not confirmed, a new confirmation email is sent." , registrationController.register(request3));

        RegistrationRequest request4 = new RegistrationRequest("Can", "tarık", "email2@test.com", "pass");
        registrationController.register(request4);
        assertEquals(1, appUserService.enableAppUser("email2@test.com"));
        assertThrows(IllegalStateException.class , () -> registrationController.register(request4));
    }

    @Test
    @Order(3)
    void testLoadUserByUsername() throws UsernameNotFoundException {
        assertThrows(UsernameNotFoundException.class, () -> appUserService.loadUserByUsername("jane.8doe@example.org"));
        appUserService.loadUserByUsername("email@test.com");
    }

    @Test
    @Order(4)
    void testEnableAppUser() {
        assertEquals(1, appUserService.enableAppUser("email@test.com"));
        assertEquals(0, appUserService.enableAppUser("bugalum@buya.bu"));
    }

    @Test
    @Order(5)
    void testChangePassword() {
        assertEquals(1, appUserService.changePassword("email@test.com", "pass"));
        assertThrows(IllegalStateException.class , () -> appUserService.changePassword("bugalum@buya.bu", "pass"));
    }

    @Test
    @Order(6)
    void testUserExists() {
        assertTrue(appUserService.userExists("email@test.com"));
        assertFalse(appUserService.userExists("bugalum@buya.bu"));
    }
    @Test
    @Order(7)
    void testCreateProject() {
        AppUser appUser = appUserService.loadUserByUsername("email@test.com");
        assertTrue(appUserService.getProjectList(appUser.getId()).isEmpty());

        Project project = projectService.createProject("project test 1", appUser);
        assertNotNull(project.getCreatedAt());
        assertNull(project.getPlannedTo());
        assertNull(project.getEndedAt());
        assertEquals(appUser, project.getAdmin());
        assertFalse(project.getMembers().isEmpty());
        assertEquals("project test 1", project.getName());
        assertNotNull(project.getId());
    }

    @Test
    @Order(8)
    void testGetProjectList() {
        AppUser appUser = appUserService.loadUserByUsername("email@test.com");
        assertFalse(appUserService.getProjectList(appUser.getId()).isEmpty());

        AppUser appUser2 = appUserService.loadUserByUsername("email2@test.com");
        assertTrue(appUserService.getProjectList(appUser2.getId()).isEmpty());

        assertThrows(IllegalArgumentException.class, () -> appUserService.getProjectList(123L));
    }

    @Test
    @Order(10)
    void testAddMember() {
        AppUser appUser = appUserService.loadUserByUsername("email2@test.com");
        assertTrue(projectService.addMember(1L, appUser));
    }

    /**
     * Method under test: {@link EmailValidator#test(String)}
     */
    @Test
    void testEmailTest() {
        assertTrue(emailValidator.test("java@lang.String"));
    }

    /**
     * Method under test: {@link EmailValidator#test(String)}
     */
    @Test
    void testEmailTest2() {
        assertFalse(emailValidator.test("java.lang.String"));
    }

    /**
     * Method under test: {@link EmailValidator#test(String)}
     */
    @Test
    void testEmailTest3() {
        assertFalse(emailValidator.test("@java.lang.String"));
    }
}
