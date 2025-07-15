package com.example.restaurantmanagement.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter,
                             JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                             UserDetailsService userDetailsService,
                             PasswordEncoder passwordEncoder) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    //  Cấu hình filter chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/tables/**").permitAll()
                        .requestMatchers("/api/staff-shifts/**").permitAll()
                        .requestMatchers("/api/recommendation/**").permitAll()
                        .requestMatchers("/api/accounts/login/**").permitAll()
                        .requestMatchers("/api/order-details/**").permitAll()
                        .requestMatchers("/api/payments/**").permitAll()
                        .requestMatchers("/api/staffs/**").permitAll()
                        .requestMatchers("/api/feedbacks/**").permitAll()
                        .requestMatchers(" /api/reservations/**").permitAll()
                        .requestMatchers("/api/images/**").permitAll()
                        .requestMatchers("/api/menu-items/**").permitAll()
                        .requestMatchers("/api/customers/**").permitAll()
                        .requestMatchers("/api/categories/**").permitAll()
                        .requestMatchers("/api/products/**").permitAll()
                        .requestMatchers("/api/product-transactions/**").permitAll()
                        .requestMatchers("/api/accounts/register/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tables/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/menu-items/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/orders/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/order-details/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/payments/**").hasRole("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/api/reservations/**").hasRole("CUSTOMER")
                        .requestMatchers("/api/tables/**").hasRole("STAFF")
                        .requestMatchers("/api/staffs/**").hasRole("STAFF")
                        .requestMatchers("/api/menu-items/**").hasRole("STAFF")
                        .requestMatchers("/api/orders/**").hasRole("STAFF")
                        .requestMatchers("/api/order-details/**").hasRole("STAFF")
                        .requestMatchers("/api/payments/**").hasRole("STAFF")
                        .requestMatchers("/api/reservations/**").hasRole("STAFF")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    //  Provider để dùng PasswordEncoder + UserDetailsService
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    //  Cho phép Spring gọi login logic
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
