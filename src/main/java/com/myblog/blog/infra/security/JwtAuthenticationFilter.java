package com.myblog.blog.infra.security;

import com.myblog.blog.model.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
//É um filtro Spring Security que intercepta cada requisição HTTP e verifica a presença de um token JWT no cabeçalho de autorização.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    // Método herdado de OncePerRequestFilter que executa a lógica de filtragem para cada requisição.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Recupera o token JWT da requisição HTTP.
        var tokenJWT = recoverToken(request);

        //Verifica se o token JWT foi recuperado com sucesso.
        if (tokenJWT != null){

            //Obtém o nome de usuário (subject) do token JWT usando o método getUsernameFromToken do serviço JwtService.
            var subject = jwtService.getUsernameFromToken(tokenJWT);

            //Busca o usuário no banco de dados pelo email obtido do token JWT.
            var user = userRepository.findByEmail(subject);

            //Cria um objeto de autenticação UsernamePasswordAuthenticationToken com base no usuário encontrado e suas autorizações.
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        }

        //Continua o fluxo da requisição atual.
        filterChain.doFilter(request, response);
    }

    //Método privado que extrai e retorna o token JWT do cabeçalho de autorização da requisição HTTP.
    private String recoverToken(HttpServletRequest request){
        // Obtém o valor do cabeçalho Authorization da requisição HTTP.
        var authorizationHeader = request.getHeader("Authorization");

        //Verifica se o cabeçalho de autorização não é nulo.
        if (authorizationHeader != null){
            //Remove o prefixo "Bearer" do valor do cabeçalho (se presente) e retorna o token JWT.
            return authorizationHeader.replace("Bearer", "").trim();
        }

        //Retorna null se o cabeçalho de autorização não estiver presente ou não contiver um token JWT.
        return null;
    }
}
