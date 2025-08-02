package com.manager.task.services;


import com.manager.task.dtos.UserRequest;
import com.manager.task.dtos.UserResponse;
import com.manager.task.entities.User;
import com.manager.task.exceptions.UserNotFoundException;
import com.manager.task.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    public User mapToUser(User user , UserRequest request){
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return user;
    }

    public UserResponse mapToResponse(User user){
        return new UserResponse(user.getId(),user.getName(), user.getEmail());
    }

    private final UserRepository userRepository;

    public void addUser(UserRequest request){
        userRepository.save(mapToUser(new User(),request));
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
}
