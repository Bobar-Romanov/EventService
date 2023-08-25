package com.service.EventMicroservice.config;

import com.service.EventMicroservice.utils.SSOAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final RestTemplate restTemplate;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //http.authenticationProvider(ssoAuthenticationProvider)
                http.addFilterBefore(new SSOAuthenticationFilter(restTemplate), UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.DELETE,"/api/event/*")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated());

        http
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers("/api/***")
                );



        return http.build();

    }





}
