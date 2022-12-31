package com.agilesekeri.asugar_api;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.repository.ProjectRepository;
import com.agilesekeri.asugar_api.service.AppUserService;
import com.agilesekeri.asugar_api.email.EmailValidator;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.controller.ProjectController;
import com.agilesekeri.asugar_api.service.ProjectService;
import com.agilesekeri.asugar_api.controller.RegistrationController;
import com.agilesekeri.asugar_api.model.request.RegistrationRequest;
import com.agilesekeri.asugar_api.service.RegistrationService;
import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

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
    private RegistrationService registrationService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectController projectController;
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @Order(1)
    void contextLoads() {

    }

    @Test
    @Order(2)
    void testRegister() throws IOException {
        RegistrationRequest request = new RegistrationRequest("Enes", "Onur", "email@test.com", "pass");
        assertEquals("A confirmation email is sent" , registrationService.register(request).getFirst());

        RegistrationRequest request2 = new RegistrationRequest("Enes", "Onur", "@test.com", "pass");
        assertThrows(IllegalStateException.class , () -> registrationService.register(request2));

        RegistrationRequest request3 = new RegistrationRequest("Enes", "Onur", "email@test.com", "pass");
        assertEquals("The given email is already registered but not confirmed, a new confirmation email is sent." , registrationService.register(request3).getFirst());

        RegistrationRequest request4 = new RegistrationRequest("Can", "tarık", "email2@test.com", "pass");
        registrationService.register(request4);
        assertEquals(1, appUserService.enableAppUser("email2@test.com"));
        assertEquals("The given email is already registered.", registrationService.register(request4).getFirst());
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
        AppUserEntity appUser = appUserService.loadUserByUsername("email@test.com");
        assertTrue(projectService.getUserProjects(appUser.getId()).isEmpty());

        ProjectEntity project = projectService.createProject("project test 1", appUser);
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
        AppUserEntity appUser = appUserService.loadUserByUsername("email@test.com");
        assertFalse(projectService.getUserProjects(appUser.getId()).isEmpty());

        AppUserEntity appUser2 = appUserService.loadUserByUsername("email2@test.com");
        LoggerFactory.getLogger(AsugarApiApplicationTests.class).info(Arrays.toString(projectService.getUserProjects(appUser2.getId()).toArray()));
        assertTrue(projectService.getUserProjects(appUser2.getId()).isEmpty());

        assertTrue(projectService.getUserProjects(-1L).isEmpty());
    }

    @Test
    @Order(10)
    void testAddMember() {
        AppUserEntity appUser = appUserService.loadUserByUsername("email2@test.com");
        assertTrue(projectService.addMember(1L, appUser));
    }

    @Test
    @Order(11)
    void testRemoveMember() {
        AppUserEntity appUser = appUserService.loadUserByUsername("email2@test.com");
        assertTrue(projectService.removeMember(1L, appUser));
        assertFalse(projectService.getUserProjects(appUser.getId())
                .contains(projectRepository.findById(1L).get()));
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
