package com.myblog.blog.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//Configura a segurança da aplicação usando o Spring Security.
// Ela define políticas como desativação de CSRF, gerenciamento de sessões sem estado,
// autorização de endpoints específicos sem autenticação, e exige autenticação para qualquer outra requisição.
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    //Método de configuração que retorna um SecurityFilterChain, que define a lógica de segurança HTTP.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //Desabilita a proteção CSRF (Cross-Site Request Forgery).
        return http.csrf(csrf -> csrf.disable())
                //Configura a política de gerenciamento de sessão como STATELESS, indicando que não se deve criar sessões HTTP para as requisições.
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Configura como devem ser autorizadas as requisições HTTP:
                .authorizeHttpRequests(req -> {
                    //Permite acesso ao endpoint /login sem autenticação.
                    req.requestMatchers("/login").permitAll();
                    req.requestMatchers(HttpMethod.POST, "/users").permitAll();

                    //Permite acesso aos endpoints relacionados à documentação Swagger sem autenticação.
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();

//                    // Permite acesso a listagem de usuários e visualização de usuário por ID para ROLE_USER e ROLE_ADMIN
//                    req.requestMatchers("/users/**").hasAnyRole("USER", "ADMIN");
//
//                    // Permite acesso a listagem de notícias e visualização de notícias por ID para USER e ADMIN
//                    req.requestMatchers("/articles/**").hasAnyRole("USER", "ADMIN");
//
//                    // Restringe postagem, edição e exclusão de notícias apenas para ADMIN
//                    req.requestMatchers(HttpMethod.POST, "/articles").hasRole("ADMIN");
//                    req.requestMatchers(HttpMethod.PUT, "/articles/**").hasRole("ADMIN");
//                    req.requestMatchers(HttpMethod.DELETE, "/articles/**").hasRole("ADMIN");
//
//                    // Restringe edição e exclusão de usuários apenas para ADMIN
//                    req.requestMatchers(HttpMethod.PUT, "/users/**").hasRole("ADMIN");
//                    req.requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN");

                    // Requer autenticação para qualquer outra requisição.
                    req.anyRequest().authenticated();
                })
                //Adiciona o filtro JwtAuthenticationFilter antes do filtro padrão UsernamePasswordAuthenticationFilter,
                // para processar a autenticação com base em tokens JWT.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    //Configura o gerenciador de autenticação usando a configuração fornecida pelo Spring Security.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    //Configura um codificador de senha BCryptPasswordEncoder, que será usado para codificar e verificar senhas no sistema.
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
