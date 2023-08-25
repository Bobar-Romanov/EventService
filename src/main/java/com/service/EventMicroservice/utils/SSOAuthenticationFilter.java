package com.service.EventMicroservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
public class SSOAuthenticationFilter extends OncePerRequestFilter {


    private final RestTemplate restTemplate;


    private String extractTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }

    private List<SimpleGrantedAuthority> authenticateWithSso(String token) throws JsonProcessingException {
        log.info("request preparation: {}", "token = " + token);

        String ssoServiceUrl = "http://localhost:8080/api/auth/check-token?token=" + token;
        String jsonResponse = restTemplate.getForObject(ssoServiceUrl, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        CustomGrantedAuthorityDeserializer[] deserializedArray = objectMapper.readValue(jsonResponse, CustomGrantedAuthorityDeserializer[].class);

        List<SimpleGrantedAuthority> authorities = Arrays.stream(deserializedArray)
                .map(customAuthority -> new SimpleGrantedAuthority(customAuthority.getAuthority()))
                .collect(Collectors.toList());

        return authorities;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
            String token = extractTokenFromRequest(request);
            if (token != null) {
                List<SimpleGrantedAuthority> authorities = authenticateWithSso(token);
                if (authorities != null && !authorities.isEmpty()) {
                    SsoAuthenticationToken authentication = new SsoAuthenticationToken(token, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        filterChain.doFilter(request, response);
    }
}
