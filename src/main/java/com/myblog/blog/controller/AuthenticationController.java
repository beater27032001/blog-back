package com.myblog.blog.controller;

import com.myblog.blog.infra.security.DataTokenJwt;
import com.myblog.blog.infra.security.JwtService;
import com.myblog.blog.model.user.AuthenticationData;
import com.myblog.blog.model.user.User;
import com.myblog.blog.model.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthenticationData authData){
        try{
            var authenticationToken = new UsernamePasswordAuthenticationToken(authData.email(), authData.password());
            var authentication = authenticationManager.authenticate(authenticationToken);

            var tokenJwt = jwtService.generateToken((User) authentication.getPrincipal());

            return ResponseEntity.ok(new DataTokenJwt(tokenJwt));
        }catch (Exception e){
            e.printStackTrace();

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
