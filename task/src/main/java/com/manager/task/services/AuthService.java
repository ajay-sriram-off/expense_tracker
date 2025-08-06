package com.manager.task.services;

import com.manager.task.dtos.AuthResponse;
import com.manager.task.dtos.LoginRequest;
import com.manager.task.dtos.RegisterRequest;
import com.manager.task.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request){
        User user = userService.registerUser(request);
        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(LoginRequest request){

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
        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}
