package com.myblog.blog.infra.config;

import com.myblog.blog.model.Role;
import com.myblog.blog.model.user.User;
import com.myblog.blog.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//CommandLineRunner é uma interface funcional do Spring Boot que permite executar código ao iniciar a aplicação.
@Component
public class InitialDataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public InitialDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe um usuário com o email pré-cadastrado
        if (userRepository.findByEmail("josecarlos2001paiva@gmail.com") == null) {
            // Cria o usuário com ROLE_ADMIN
            User adminUser = new User();
            adminUser.setName("José Carlos Paiva Santos");
            adminUser.setEmail("josecarlos2001paiva@gmail.com");
            adminUser.setPassword(passwordEncoder.encode("Jose123"));
            adminUser.setPhone("99 999999999");
            adminUser.setRole(Role.ROLE_ADMIN);

            // Salva o usuário no banco de dados
            userRepository.save(adminUser);

            System.out.println("Usuário admin pré-cadastrado com sucesso!");
        } else {
            System.out.println("Usuário admin já está cadastrado.");
        }
    }
}
