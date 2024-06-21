package com.myblog.blog.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.myblog.blog.model.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.stream.Collectors;

@Service
//É responsável por gerar tokens JWT com base nas informações do usuário, usando uma chave secreta configurada (jwtSecret).
public class JwtService {

    //Injeção do valor da propriedade blog.security.jwt.secret do arquivo de configuração do Spring na variável jwtSecret.
    // Essa propriedade geralmente contém a chave secreta usada para assinar e verificar tokens JWT.
    @Value("${blog.security.jwt.secret}")
    private String jwtSecret;

    // Constante que define o emissor (issuer) do token JWT como "API MyBlog"
    private static final String ISSUER = "API MyBlog";

    //Método responsável por gerar um token JWT para o usuário fornecido.
    public String generateToken(User user){
        try{
            //Cria um algoritmo de assinatura HMAC com a chave secreta (jwtSecret) fornecida
            var algorithm = Algorithm.HMAC256(jwtSecret);
            //Inicia a construção do token JWT
            return JWT.create()
                    //Define o emissor do token como "API MyBlog"
                    .withIssuer(ISSUER)
                    //Define o assunto (subject) do token como o nome de usuário do usuário fornecido
                    .withSubject(user.getUsername())
                    .withClaim("roles", user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
//                    .withExpiresAt(Date.from(expirationData()))
                    //Finaliza e assina o token usando o algoritmo especificado (HMAC256 com a chave secreta)
                    .sign(algorithm);
        }catch (JWTCreationException exception){
            throw new RuntimeException("Error to generate token JWT", exception);
        }
    }

    //Método responsável por verificar e obter o nome de usuário do token JWT fornecido.
    public String getUsernameFromToken(String tokenJWT){
        try {
            var algorithm = Algorithm.HMAC256(jwtSecret);
            // Inicia a verificação do token JWT usando o algoritmo especificado.
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    //Constrói e verifica o token JWT fornecido (tokenJWT), lançando uma exceção JWTVerificationException se o token for inválido ou expirado.
                    .build().verify(tokenJWT)
                    //Obtém o assunto (subject) do token, que neste caso é o nome de usuário contido no token JWT.
                    .getSubject();
        }catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT is invalid or expired.");
        }
    }



    private Instant expirationData() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
