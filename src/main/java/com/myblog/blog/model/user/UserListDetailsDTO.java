package com.myblog.blog.model.user;

public record UserListDetailsDTO( Long id,
                                  String name,
                                  String email,
                                  String phone,
                                  String role) {

    public UserListDetailsDTO(User user){
        this(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole().name());
    }
}
