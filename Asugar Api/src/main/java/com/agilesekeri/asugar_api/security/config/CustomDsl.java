package com.agilesekeri.asugar_api.security.config;

import com.agilesekeri.asugar_api.security.authentication.CustomAuthenticationFilter;
import com.agilesekeri.asugar_api.security.authentication.CustomAuthorizationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomDsl extends AbstractHttpConfigurer<CustomDsl, HttpSecurity> {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        CustomAuthenticationFilter authenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        authenticationFilter.setFilterProcessesUrl("/login");
        CustomAuthorizationFilter authorizationFilter = new CustomAuthorizationFilter();
        http.addFilter(authenticationFilter);
        http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    public static CustomDsl customDsl() {
        return new CustomDsl();
    }
}
