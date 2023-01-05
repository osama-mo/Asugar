package com.agilesekeri.asugar_api;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import com.agilesekeri.asugar_api.model.dto.AbstractIssueDTO;
import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.model.entity.IssueEntity;
import com.agilesekeri.asugar_api.model.entity.SprintEntity;
import com.agilesekeri.asugar_api.model.enums.IssueTypeEnum;
import com.agilesekeri.asugar_api.model.request.IssueCreateRequest;
import com.agilesekeri.asugar_api.repository.ProjectRepository;
import com.agilesekeri.asugar_api.service.*;
import com.agilesekeri.asugar_api.email.EmailValidator;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.controller.ProjectController;
import com.agilesekeri.asugar_api.controller.RegistrationController;
import com.agilesekeri.asugar_api.model.request.RegistrationRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AsugarApiApplicationTests {
    @Autowired
    private AppUserService appUserService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SprintService sprintService;

    @Autowired
    private IssueService issueService;


    @Autowired
    private EmailValidator emailValidator;

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
        assertEquals("email not valid" , registrationService.register(request2).getFirst());

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
        RegistrationRequest request = new RegistrationRequest("user", "exist", "user@exist.test", "pass");
        registrationService.register(request);
        assertTrue(appUserService.userExists("user@exist.test"));
        assertFalse(appUserService.userExists("random@buya.bu"));
    }

    @Test
    @Order(7)
    void testCreateProject() {
        RegistrationRequest request = new RegistrationRequest("project", "create", "project@create.test", "pass");
        RegistrationRequest request2 = new RegistrationRequest("project2", "create", "project2@create.test", "pass");
        registrationService.register(request);
        registrationService.register(request2);
        appUserService.enableAppUser("project@create.test");
        appUserService.enableAppUser("project2@create.test");
        ProjectEntity project = projectService.createProject("project test", appUserService.loadUserByUsername("project@create.test"));
        projectService.addMember(project.getId(), "project2@create.test");

        assertThrows(IllegalArgumentException.class,
                () -> projectService.createProject(
                        "project test",
                        appUserService.loadUserByUsername("project@create.test")));

        assertFalse(projectService.getUserProjects(appUserService.loadUserByUsername("project@create.test").getId()).isEmpty());
        assertFalse(projectService.getUserProjects(appUserService.loadUserByUsername("project2@create.test").getId()).isEmpty());
        assertTrue(projectService.getUserProjects(-1L).isEmpty());
    }

    @Test
    @Order(8)
    void testGetProjectList() {
        RegistrationRequest request = new RegistrationRequest("project", "list", "project@list.test", "pass");
        RegistrationRequest request2 = new RegistrationRequest("project2", "list", "project2@list.test", "pass");
        registrationService.register(request);
        registrationService.register(request2);
        appUserService.enableAppUser("project@list.test");
        appUserService.enableAppUser("project2@list.test");
        ProjectEntity project = projectService.createProject("project test", appUserService.loadUserByUsername("project@list.test"));
        projectService.addMember(project.getId(), "project2@list.test");

        assertFalse(projectService.getUserProjects(appUserService.loadUserByUsername("project@list.test").getId()).isEmpty());
        assertFalse(projectService.getUserProjects(appUserService.loadUserByUsername("project2@list.test").getId()).isEmpty());
        assertTrue(projectService.getUserProjects(-1L).isEmpty());
    }

    @Test
//    @Order(8)
    void testRemoveProject() {
        RegistrationRequest request = new RegistrationRequest("project", "remove", "project@remove.test", "pass");
        RegistrationRequest request2 = new RegistrationRequest("project2", "remove", "project2@remove.test", "pass");
        registrationService.register(request);
        registrationService.register(request2);
        appUserService.enableAppUser("project@remove.test");
        appUserService.enableAppUser("project2@remove.test");
        ProjectEntity project = projectService.createProject("project test", appUserService.loadUserByUsername("project@remove.test"));
        projectService.addMember(project.getId(), "project2@remove.test");

        projectService.deleteProject(project.getId());
        assertTrue(appUserService.loadUserByUsername("project@remove.test").getProjects().isEmpty());
        assertTrue(appUserService.loadUserByUsername("project2@remove.test").getProjects().isEmpty());
    }

    @Test
    @Order(10)
    void testAddMember() {
        RegistrationRequest request = new RegistrationRequest("member", "add", "member@add.test", "pass");
        RegistrationRequest request2 = new RegistrationRequest("member2", "add", "member2@add.test", "pass");
        registrationService.register(request);
        registrationService.register(request2);
        appUserService.enableAppUser("member@add.test");
        appUserService.enableAppUser("member2@add.test");
        ProjectEntity project = projectService.createProject("project test", appUserService.loadUserByUsername("member@add.test"));

        assertTrue(projectService.addMember(project.getId(), "member2@add.test"));
        assertTrue(projectService.getUserProjects(
                appUserService.loadUserByUsername("member2@add.test").getId())
                .contains(project));
        assertFalse(projectService.addMember(project.getId(), "member2@add.test"));
        assertThrows(UsernameNotFoundException.class, () -> projectService.addMember(project.getId(), "random@buya.bu"));
    }

    @Test
    @Order(11)
    void testRemoveMember() {
        RegistrationRequest request = new RegistrationRequest("member", "remove", "member@remove.test", "pass");
        RegistrationRequest request2 = new RegistrationRequest("member2", "remove", "member2@remove.test", "pass");
        registrationService.register(request);
        registrationService.register(request2);
        appUserService.enableAppUser("member@remove.test");
        appUserService.enableAppUser("member2@remove.test");
        ProjectEntity project = projectService.createProject("project test", appUserService.loadUserByUsername("member@remove.test"));
        projectService.addMember(project.getId(), "member2@remove.test");

        assertTrue(projectService.removeMember(project.getId(), "member2@remove.test"));
        assertFalse(projectService.getUserProjects(
                appUserService.loadUserByUsername("member2@remove.test").getId())
                .contains(project));
        assertFalse(projectService.removeMember(project.getId(), "member2@remove.test"));
        assertThrows(UsernameNotFoundException.class, () -> projectService.removeMember(project.getId(), "random@buya.bu"));
    }

    @Test
    void testFinishSprint() {
        RegistrationRequest request = new RegistrationRequest("sprint", "finish", "sprint@finish.test", "pass");
        registrationService.register(request);
        appUserService.enableAppUser("sprint@finish.test");
        ProjectEntity project = projectService.createProject("sprint finish test", appUserService.loadUserByUsername("sprint@finish.test"));
        SprintEntity active = projectService.getActiveSprint(project.getId());
        SprintEntity next = projectService.getNextSprint(project.getId());

        projectService.finishActiveSprint(project.getId());
        assertEquals(next, projectService.getActiveSprint(project.getId()));
        assertTrue(sprintService.getSprint(active.getId()).getEndedAt().isBefore(LocalDateTime.now()));
    }

    @Test
    void testCreateIssue() {
        RegistrationRequest request = new RegistrationRequest("issue", "create", "issue@create.test", "pass");
        registrationService.register(request);
        appUserService.enableAppUser("issue@create.test");
        ProjectEntity project = projectService.createProject("issue create test", appUserService.loadUserByUsername("issue@create.test"));

        IssueCreateRequest issueCreateRequest = IssueCreateRequest.builder()
                .issueType(IssueTypeEnum.STORY)
                .description("This is an issue created to test issue creation")
                .title("issue create test")
//                .projectId(project.getId())
                .manHour(33)
                .sprint("active")
                .build();

        AbstractIssue issue = issueService.createIssue(
                issueCreateRequest,
                appUserService.loadUserByUsername("issue@create.test"),
                project);

        AbstractIssueDTO target = issueService.getIssueInfo(issue.getId());

        assertTrue(projectService.getAllIssues(project.getId())
                .contains(target));
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
