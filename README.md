# User Service

Microsserviço responsável pelo cadastro, autenticação e autorização de usuários da plataforma de agendamento de tarefas. Faz parte de uma arquitetura distribuída composta por múltiplos serviços independentes (`user`, `task-scheduler`, `notification`, `bff-task-scheduler`).

## Responsabilidades

- Cadastro e gerenciamento de usuários
- Autenticação via login/senha
- Emissão e validação de tokens JWT, utilizados pelos demais serviços da plataforma
- Exposição de contrato de API documentado via OpenAPI/Swagger

## Stack técnica

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 23 |
| Framework | Spring Boot 3.5.5 |
| Persistência | Spring Data JPA + PostgreSQL |
| Segurança | Spring Security + JWT (JJWT) |
| Documentação de API | springdoc-openapi (Swagger UI) |
| Build | Gradle |
| CI/CD | GitHub Actions |
| Boilerplate | Lombok |

## Arquitetura

Este serviço atua como fonte da verdade (source of truth) para identidade dos usuários. Outros serviços da plataforma (como `task-scheduler`) consomem os tokens JWT emitidos aqui para autorizar requisições, sem necessidade de chamada síncrona a cada validação.

## Como executar localmente

### Pré-requisitos
- Java 23 (JDK)
- PostgreSQL em execução localmente ou via container
- Gradle (o wrapper `./gradlew` já está incluso no projeto)

### Passos

```bash
# Clonar o repositório
git clone https://github.com/gilbertneto/user.git
cd user

# Configurar variáveis de ambiente / application.properties
# (ver seção "Configuração" abaixo)

# Build
./gradlew build

# Executar
./gradlew bootRun
```

### Configuração

Defina as seguintes variáveis de ambiente (ou configure em `application.properties`/`application.yml`):

```
DB_URL=jdbc:postgresql://localhost:5432/user_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=<chave-secreta-para-assinatura-dos-tokens>
JWT_EXPIRATION=3600000
```

## Documentação da API

Após subir a aplicação, a documentação interativa (Swagger UI) fica disponível em:

```
http://localhost:8080/swagger-ui/index.html
```

## Testes

A implementar...

## CI/CD

O repositório possui pipeline configurada em `.github/workflows`, executando build e testes automaticamente a cada push/pull request.

## Roadmap / melhorias futuras

- [ ] Documentar contratos de request/response no README
- [ ] Adicionar exemplos de payload de autenticação
- [ ] Adicionar Dockerfile e instruções de execução via container
- [ ] Adicionar Testes Automatizados (JUnit) Mockito
