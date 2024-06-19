package com.myblog.blog.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//Configura a segurança da aplicação usando o Spring Security.
// Ela define políticas como desativação de CSRF, gerenciamento de sessões sem estado,
// autorização de endpoints específicos sem autenticação, e exige autenticação para qualquer outra requisição.
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

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
                    //Permite acesso aos endpoints relacionados à documentação Swagger sem autenticação.
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();
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
