package com.myblog.blog.service;

import com.myblog.blog.model.Role;
import com.myblog.blog.model.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserListDetailsDTO createUser(UserCreateDTO userCreateDTO){
        User user = new User();
        user.setName(userCreateDTO.name());
        user.setEmail(userCreateDTO.email());
        user.setPassword(passwordEncoder.encode(userCreateDTO.password()));
        user.setPhone(userCreateDTO.phone());
        user.setRole(Role.ROLE_USER); // Default role

        user = userRepository.save(user);

        return new UserListDetailsDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole().name());
    }

    public UserListDetailsDTO updateUser(UserUpdateDTO userUpdateDTO){
        User user = userRepository.findById(userUpdateDTO.id()).orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(userUpdateDTO.name());
        user.setEmail(userUpdateDTO.email());
        user.setPhone(userUpdateDTO.phone());

        user = userRepository.save(user);

        return new UserListDetailsDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole().name());
    }

    public UserListDetailsDTO getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserListDetailsDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole().name());
    }

    public List<UserListDetailsDTO> getAll(){
        return userRepository.findAll().stream()
                .map(user -> new UserListDetailsDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole().name()))
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

}
