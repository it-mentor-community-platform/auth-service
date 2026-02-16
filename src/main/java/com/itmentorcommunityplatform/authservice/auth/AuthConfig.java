package com.itmentorcommunityplatform.authservice.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class AuthConfig {

    @Bean
    public List<Long> adminsIds() {
        return new ArrayList<>(Arrays.asList(
                197342870L, // zhukovsd
                6926659633L, // oneQwerty2
                107541084L // PAlex_89
        ));
    }
}
