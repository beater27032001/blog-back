package com.myblog.blog.model.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("User not Found.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), getAuthorities(user)
        );

    }

    //Método responsável por obter as autorizações (papéis) do usuário.
    //Cria um conjunto (Set) de GrantedAuthority, que é uma interface do Spring Security para representar papéis concedidos a um usuário.
    //Adiciona ao conjunto um SimpleGrantedAuthority com o nome da permissão (ROLE_USER, ROLE_ADMIN, etc.) obtida do objeto user.
    private Collection<GrantedAuthority> getAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return authorities;
    }
}
