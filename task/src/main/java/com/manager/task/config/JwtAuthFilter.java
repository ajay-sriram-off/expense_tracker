package com.manager.task.config;

import com.manager.task.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Check header
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
             //  If authHeader is null, no Authorization header was sent at all.
            //we only accept token in this format :  Bearer <token>

            /* The logic is:
                - No JWT? → “Not my job. Pass it along.”
                - Yes JWT? → “Okay, now I’ll try to validate it before passing along.”
                - we do this because this method is only to filter JWT , if there is no JWT , no point in filtering
                - so pass it to the next filter cuz we don't know what kind of API its hitting , maybe its auth call which doesnt need JWT
            */
            filterChain.doFilter(request, response);  // and this method is responsible for passing it to next filter
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        /* SecurityContextHolder → SecurityContext → Authentication → Principal (UserDetails)
              - Authentication = SecurityContextHolder.getContext().getAuthentication();
              - auth.getPrincipal() => Our User object
              - auth.getAuthorities() => ROLE_USER
         */
        System.out.println("JWT AUTHFILTER : "+ jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Here SecurityContextHolder.getContext().getAuthentication() == null helps us prevent overriding existing authentication.
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) { // checking if token is valid
                UsernamePasswordAuthenticationToken authToken = // we are creating a token of our own using raw JWT
                        new UsernamePasswordAuthenticationToken( // this is usually done by spring on its own in spring security login form but here we want to use our own login form
                                userDetails, // principal (your User entity)
                                null,   // no password anymore - security practices
                                userDetails.getAuthorities() // setting roles
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // adds extra metadata from the HttpServletRequest (IP, session, etc.) into that token.

                SecurityContextHolder.getContext().setAuthentication(authToken); // Finally, you push it into Spring Security’s global context (Note : not JWT token)
            }
        }

        filterChain.doFilter(request, response); // need to move this request to next filter
    }

}
