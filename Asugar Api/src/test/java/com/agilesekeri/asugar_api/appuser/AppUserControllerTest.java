package com.agilesekeri.asugar_api.appuser;

import com.agilesekeri.asugar_api.project.ProjectRepository;
import com.agilesekeri.asugar_api.project.ProjectService;

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

