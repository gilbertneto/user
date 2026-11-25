# user

Microsserviço de gestão de usuários, autenticação e autorização do ecossistema de agendamento de tarefas.

## Responsabilidades

- Cadastro, consulta, atualização e remoção de usuários.
- Autenticação via login com geração de token **JWT**.
- Gestão de endereços e telefones vinculados ao usuário.
- Consulta de endereço por CEP através da API pública **ViaCEP** (via `OpenFeign`).

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 23 |
| Framework | Spring Boot 3.5.5 |
| Segurança | Spring Security, JWT (`jjwt`) |
| Persistência | Spring Data JPA, PostgreSQL |
| Integração | Spring Cloud OpenFeign (ViaCEP) |
| Documentação | springdoc-openapi / Swagger |
| Container | Docker |

## Endpoints principais (`/usuario`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/usuario` | Cria um novo usuário |
| `POST` | `/usuario/login` | Autentica e retorna um token JWT |
| `GET` | `/usuario?email=` | Busca usuário por e-mail |
| `PUT` | `/usuario` | Atualiza dados do usuário autenticado |
| `DELETE` | `/usuario/{email}` | Remove usuário por e-mail |
| `POST` / `PUT` | `/usuario/endereco` | Cadastra / atualiza endereço |
| `POST` / `PUT` | `/usuario/telefone` | Cadastra / atualiza telefone |
| `GET` | `/usuario/endereco/{cep}` | Consulta endereço por CEP (ViaCEP) |

## Configuração

Variáveis definidas em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/db_nome
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
viacep.url=https://viacep.com.br
```

Ajuste usuário/senha/URL do banco conforme o seu ambiente (variáveis de ambiente são recomendadas em produção).

## 🐳 Docker

O projeto já inclui `Dockerfile` (multi-stage: build com Gradle + runtime em `openjdk:23-jdk`) e `docker-composer.yml` (sobe a aplicação **e** o PostgreSQL juntos).

### Opção recomendada — Docker Compose (app + banco)

```bash
docker compose -f docker-composer.yml up --build
```

| Serviço | Porta host | Descrição |
|---|---|---|
| `app` | `8080` | API do microsserviço `user` |
| `db` | `5432` | PostgreSQL (`db_nome` / `seu_usuario` / `sua_senha`) |

### Alternativa — build e run manual (banco já disponível)

```bash
docker build -t user-service .

docker run --rm -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/db_nome \
  -e SPRING_DATASOURCE_USERNAME=seu_usuario \
  -e SPRING_DATASOURCE_PASSWORD=sua_senha \
  user-service
```

> Em Linux, substitua `host.docker.internal` pelo IP da bridge do Docker (`172.17.0.1`) ou rode o banco no mesmo `--network`.

## Como rodar localmente (sem Docker)

Requer PostgreSQL ativo:

```bash
./gradlew bootRun
```

Aplicação sobe em `http://localhost:8080`.

## Documentação da API

Com a aplicação em execução, a documentação Swagger/OpenAPI fica disponível em:

```
http://localhost:8080/swagger-ui.html
```

## Integração com outros serviços

Este serviço é consumido via `OpenFeign` pelo **bff-task-scheduler** (gateway) e pelo **task-scheduler** (validação/consulta de usuário autenticado).
