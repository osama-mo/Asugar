package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.repository.AppUserRepository;
import com.agilesekeri.asugar_api.repository.ProjectRepository;
import com.agilesekeri.asugar_api.service.AppUserService;
import com.agilesekeri.asugar_api.service.ProjectService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {AppUserController.class, AppUserService.class, ProjectService.class,
        BCryptPasswordEncoder.class})
@ExtendWith(SpringExtension.class)
class AppUserControllerTest {
    @Autowired
    private AppUserController appUserController;

    @MockBean
    private AppUserRepository appUserRepository;

    @MockBean
    private ProjectRepository projectRepository;

    /**
     * Method under test: {@link AppUserController#createProject(String, HttpServletRequest)}
     */
    @Test
    void testCreateProject() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/project/create").param("name", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(appUserController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link AppUserController#deleteProject(Long, HttpServletRequest)}
     */
    @Test
    void testDeleteProject() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/project/{projectId}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(appUserController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link AppUserController#getProjectList(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testGetProjectList() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/project/list");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(appUserController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link AppUserController#refreshToken(HttpServletRequest, HttpServletResponse)}
     */
    @Test
    void testRefreshToken() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/token/refresh");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(appUserController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

