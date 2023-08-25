package com.service.EventMicroservice.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomGrantedAuthorityDeserializer {
    @JsonProperty("authority")
    private String authority;

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
