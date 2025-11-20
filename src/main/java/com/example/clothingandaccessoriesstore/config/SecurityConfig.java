package com.example.clothingandaccessoriesstore.config;

import com.example.clothingandaccessoriesstore.entity.Role;
import com.example.clothingandaccessoriesstore.security.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;
    @Value("${front.url}")
    private String frontendURL;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/user/register", "/user/authorization").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/confirmation").permitAll()
                        .requestMatchers(HttpMethod.POST, "/category/add").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/category/delete/{id}").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/category/getAll").permitAll()
                        .requestMatchers(HttpMethod.POST, "/product").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/product/delete/{id}").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/product/update").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/product/get15Product").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/grtAll").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/getById/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/getAllByPageable").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/category/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product/productBy/{name}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/getImage").permitAll()
                        .requestMatchers(HttpMethod.POST, "/liked/add").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.GET, "/liked/get/{email}").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.DELETE, "/liked/delete").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.POST, "/basket/add").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.GET, "/basket/get").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.DELETE, "/basket/delete").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.PUT, "/basket/update").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.POST, "/order/add").permitAll()
                        .requestMatchers(HttpMethod.POST, "/order/addAll").permitAll()
                        .requestMatchers(HttpMethod.GET, "/order/getAll").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                        .anyRequest().authenticated());
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendURL));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
