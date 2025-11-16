package com.example.clothingandaccessoriesstore.config;

import com.example.clothingandaccessoriesstore.security.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@RequiredArgsConstructor
public class UserDetailsConfig {
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final CustomUserDetailService customUserDetailService;

    @Bean
    public AuthenticationProvider userDetailsService() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoderConfig.passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
        return daoAuthenticationProvider;
    }
}
