package com.manager.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {
    /*
        We have created PasswordEncoder in a  separate class rather than inside Security config cuz this was causing circular issue
        SecurityConfig -> UserService -> PasswordEncoder -> SecurityConfig
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}