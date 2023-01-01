package com.agilesekeri.asugar_api.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers(
                "/registration",
                "/registration/**",
                "/login/**",
                "/user/token/refresh",
                "/password_reset").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.apply(CustomDsl.customDsl());
        http.cors().configurationSource(c -> {
            CorsConfiguration corsCfg = new CorsConfiguration();

            // All origins, or specify the origins you need
            corsCfg.addAllowedOriginPattern( "*" );

            // If you really want to allow all methods
//            corsCfg.addAllowedMethod( CorsConfiguration.ALL );

            // If you want to allow specific methods only
            corsCfg.addAllowedMethod( HttpMethod.GET );
            corsCfg.addAllowedMethod( HttpMethod.DELETE );
            corsCfg.addAllowedMethod( HttpMethod.PUT );
            corsCfg.addAllowedMethod( HttpMethod.POST );
            // ...

            return corsCfg;
        });

        return http.build();
    }
}

