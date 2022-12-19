package com.agilesekeri.asugar_api.project;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.agilesekeri.asugar_api.appuser.AppUserEntity;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

    /**
     * Method under test: {@link ProjectService#addMember(Long, AppUserEntity)}
     */
    @Test
    void testAddMember() {
        AppUserEntity appUser = new AppUserEntity();
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setId(123L);
        appUser.setLastName("Doe");
        appUser.setPassword("iloveyou");
        appUser.setProjects(new HashSet<>());
        assertThrows(IllegalArgumentException.class, () -> projectService.addMember(123L, appUser));
    }

    /**
     * Method under test: {@link ProjectService#addMember(Long, AppUserEntity)}
     */
    @Test
    void testAddMember2() {
        AppUserEntity appUser = new AppUserEntity();
        appUser.setEmail("jane.doe@example.org");
        appUser.setEnabled(true);
        appUser.setFirstName("Jane");
        appUser.setId(123L);
        appUser.setLastName("Doe");
        appUser.setPassword("iloveyou");
        appUser.setProjects(new HashSet<>());
        assertThrows(IllegalArgumentException.class, () -> projectService.addMember(2L, appUser));
    }
}

