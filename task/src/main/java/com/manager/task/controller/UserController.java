package com.manager.task.controller;

import com.manager.task.dtos.UserRequest;
import com.manager.task.dtos.UserResponse;
import com.manager.task.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // add user
    @PostMapping
    public ResponseEntity<Void> addUser(@Valid @RequestBody UserRequest request){
        userService.addUser(request);
        return ResponseEntity.noContent().build();
    }

    // update user
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id , @Valid @RequestBody UserRequest request){
        userService.updateUser(id ,request);
        return ResponseEntity.noContent().build();
    }

    // delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // get a user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
       UserResponse response= userService.getUserById(id);
       return ResponseEntity.ok(response);
    }

    // getAll users
    @GetMapping
    public ResponseEntity<Page<UserResponse>>
    getAllUser(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

}
