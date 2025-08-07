package com.manager.task.config;

import com.manager.task.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Bean
    public JwtAuthFilter jwtAuthFilter() { // this is called  FACTORY METHOD bean definition
        return new JwtAuthFilter(jwtService, userDetailsService);
         /* private final JwtAuthFilter jwtAuthFilter;
          This was how it was before .we made it into factory method  cuz it was forming a circular reference */

        /*
        Circular Reference:
            - SecurityConfig needed UserService (because you used it as userDetailsService).
            - UserService needed PasswordEncoder (which was a bean inside SecurityConfig).
            - JwtAuthFilter needed both JwtService and UserService.
            - But JwtAuthFilter itself was being auto-wired by Spring inside SecurityConfig.
        */
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer:: disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // stateless means webtoken
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()   // open for register/login
                        .requestMatchers("/categories/**").hasRole("ADMIN") // only admin can create/edit categories
                        .anyRequest().authenticated() // all other endpoints need JWT
                )
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
                 //By saying addFilterBefore we are saying Insert  JwtAuthFilter before Spring’s default login filter runs

                /* Without addFilterBefore
                    -  Spring starts running built-in filters.
                    - None of them know how to handle “Bearer …” tokens.
                    - UsernamePasswordAuthenticationFilter runs, but it only understands form login (username + password from request body). It sees no login attempt → assumes the request is anonymous.
                    - Since your endpoint requires authentication, Spring rejects it → 401 Unauthorized.
                */

               /* With addFilterBefore
                    - Your JwtAuthFilter runs first.
                    - Checks Authorization: Bearer ... Validates token.
                    - If valid → builds UsernamePasswordAuthenticationToken with user details.
                    - Puts that into SecurityContextHolder.
                    - Then Spring’s built-in filters run.
                    - When UsernamePasswordAuthenticationFilter runs,
                      it sees “Oh, there’s already a user in the SecurityContext” → it doesn’t treat request as anonymous anymore.
                */
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder  passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        /*
        We need userDetailsService and password encoder to register DaoAuthProvider as our authenticationProvider
         */
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }


}