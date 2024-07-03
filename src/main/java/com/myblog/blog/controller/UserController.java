package com.myblog.blog.controller;

import com.myblog.blog.model.user.UserCreateDTO;
import com.myblog.blog.model.user.UserListDetailsDTO;
import com.myblog.blog.model.user.UserUpdateDTO;
import com.myblog.blog.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserListDetailsDTO>> getAllUsers(){
        List<UserListDetailsDTO> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserListDetailsDTO> getUserById(@PathVariable Long id){
        UserListDetailsDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<UserListDetailsDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO){
        UserListDetailsDTO newUser = userService.createUser(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<UserListDetailsDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userUpdateDTO){
        UserListDetailsDTO userUpdate = userService.updateUser(userUpdateDTO);
        return ResponseEntity.ok(userUpdate);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
