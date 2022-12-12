package com.agilesekeri.asugar_api.registration;

import com.agilesekeri.asugar_api.appuser.AppUserRepository;
import com.agilesekeri.asugar_api.appuser.AppUserService;
import com.agilesekeri.asugar_api.email.EmailSender;
import com.agilesekeri.asugar_api.email.EmailValidator;
import com.agilesekeri.asugar_api.project.ProjectRepository;
import com.agilesekeri.asugar_api.project.ProjectService;
import com.agilesekeri.asugar_api.registration.token.RegistrationTokenRepository;
import com.agilesekeri.asugar_api.registration.token.RegistrationTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {RegistrationController.class, RegistrationService.class, AppUserService.class,
        ProjectService.class, BCryptPasswordEncoder.class, EmailValidator.class, RegistrationTokenService.class})
@ExtendWith(SpringExtension.class)
class RegistrationControllerTest {
    @MockBean
    private AppUserRepository appUserRepository;

    @MockBean
    private EmailSender emailSender;

    @MockBean
    private ProjectRepository projectRepository;

    @Autowired
    private RegistrationController registrationController;

    @MockBean
    private RegistrationTokenRepository registrationTokenRepository;

    /**
     * Method under test: {@link RegistrationController#register(RegistrationRequest)}
     */
    @Test
    void testRegister() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(
                objectMapper.writeValueAsString(new RegistrationRequest("Jane", "Doe", "jane.doe@example.org", "iloveyou")));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(registrationController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

