package com.manager.task.services;


import com.manager.task.dtos.RegisterRequest;
import com.manager.task.dtos.UserRequest;
import com.manager.task.dtos.UserResponse;
import com.manager.task.entities.User;
import com.manager.task.enums.Role;
import com.manager.task.exceptions.UserNotFoundException;
import com.manager.task.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService { // here UserDetailsService  is a Spring Security interface

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User mapToUser(User user , UserRequest request){
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return user;
    }

    public UserResponse mapToResponse(User user){
        return new UserResponse(user.getId(),user.getName(), user.getEmail());
    }

    public User registerUser(RegisterRequest request){
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole()))
                .build();

        return userRepository.save(user);
    }

    public void updateUser(Long id ,UserRequest request){
        userRepository.findById(id).map(user -> {
           return mapToUser(user ,request);
        }).orElseThrow(()-> new UserNotFoundException(id));
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public Page<UserResponse> getAllUsers(Pageable pageable){
        Page<User> user = userRepository.findAll(pageable);

        return user.map(this::mapToResponse);
    }

    public UserResponse getUserById(Long id){
       User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
       return mapToResponse(user);
    }


    @Override // overriding this method from UserDetailsService. Needed for Spring Security during login
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")); // since User entity implements UserDetails even if this returns user there won't be any problem
    }
}
