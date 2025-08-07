package com.manager.task.services;

import com.manager.task.dtos.AuthResponse;
import com.manager.task.dtos.LoginRequest;
import com.manager.task.dtos.RegisterRequest;
import com.manager.task.entities.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request,HttpServletResponse response){
        User user = userService.registerUser(request);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        Cookie cookie = new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(604800); // 7d
        cookie.setSecure(true);
        response.addCookie(cookie);
        return new AuthResponse(accessToken);
    }

    public AuthResponse login( LoginRequest request , HttpServletResponse response){

        authenticationManager.authenticate(
                /*
                  Here instead of doing the authentication ourselves we delegate that work to authenticationManager
                 */
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = (User) userService.loadUserByUsername(request.getEmail());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        System.out.println("Access : " + accessToken);
        System.out.println("Refresh : " + refreshToken);
        Cookie cookie = new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(604800); // 7d
        cookie.setSecure(true);
        response.addCookie(cookie);
         return new AuthResponse(accessToken);
    }

    public String refreshAccessToken(String refreshToken){
        if (refreshToken == null) throw new RuntimeException("Refresh token is missing");
        if (!jwtService.validateToken(refreshToken)) throw new RuntimeException("Invalid or expired refresh token");

        String username = jwtService.extractUsername(refreshToken);
        UserDetails user = userService.loadUserByUsername(username);
        String accessToken = jwtService.generateAccessToken(user);
        System.out.println("New ACCESS TOKEN : "+ accessToken);
        return accessToken;
    }


}
