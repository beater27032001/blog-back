# Blog System Back-end

Este repositório contém o código-fonte de um aplicativo back-end para um sistema de blog. Ele fornece uma API RESTful para gerenciar usuários, artigos e autenticação.

## Propósito

O propósito deste aplicativo é fornecer endpoints para operações básicas de CRUD (Create, Read, Update, Delete) para usuários e artigos, além de implementar autenticação com JWT para proteger os recursos.

## Dependências

- Java 11 ou superior
- Spring Boot 3.x
- Spring Data JPA
- H2 Database (embedded)
- Spring Security
- SpringDoc OpenAPI
- Lombok
- Spring Web

## Como Rodar

Para rodar este aplicativo localmente, siga as etapas abaixo:

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/beater27032001/blog-back.git
   cd seu-repositorio

2. **Você pode executar o aplicativo usando sua IDE preferida ou usando o Maven**

3. **Crie um schema no seu banco de dados de preferência e configure no application.properties com os seus dados**
   ```bash
      CREATE TABLE users (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
       email VARCHAR(255) UNIQUE NOT NULL,
       password VARCHAR(255) NOT NULL,
       phone VARCHAR(20) NOT NULL,
       role VARCHAR(20) NOT NULL
   );

      CREATE TABLE articles (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       title VARCHAR(255),
       content TEXT,
       author_id BIGINT NOT NULL,
       CONSTRAINT fk_author
        FOREIGN KEY (author_id)
        REFERENCES users (id)
   );

4. **Acesso à documentação da API (Swagger - opcional):**
   
    Após iniciar o aplicativo, você pode acessar a documentação da API no Swagger UI:
  
    http://localhost:8080/swagger-ui/index.html
  
    Isso abrirá a interface do Swagger UI, onde você pode visualizar e testar os endpoints da API.

## Endpoints Disponíveis

### Usuários
- **GET** /users: Retorna todos os usuários cadastrados.
- **GET** /users/{id}: Retorna um usuário pelo ID.
- **POST** /users: Cria um novo usuário (requer autenticação ROLE_ADMIN).
- **PUT** /users/{id}: Atualiza um usuário existente pelo ID (requer autenticação do usuário ou ROLE_ADMIN).
- **DELETE** /users/{id}: Exclui um usuário pelo ID (requer autenticação do usuário ou ROLE_ADMIN).

### Artigos
- **GET** /articles: Retorna todos os artigos cadastrados.
- **GET** /articles/{id}: Retorna um artigo pelo ID.
- **POST** /articles: Cria um novo artigo (requer autenticação ROLE_ADMIN).
- **PUT** /articles/{id}: Atualiza um artigo existente pelo ID (requer autenticação do autor ou ROLE_ADMIN).
- **DELETE** /articles/{id}: Exclui um artigo pelo ID (requer autenticação do autor ou ROLE_ADMIN).

## Status: Concluido

  Este projeto está concluido.



