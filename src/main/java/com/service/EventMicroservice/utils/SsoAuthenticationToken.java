package com.service.EventMicroservice.utils;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SsoAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;

    public SsoAuthenticationToken(String token) {
        super(null);
        this.token = token;
    }

    public SsoAuthenticationToken(String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }
}