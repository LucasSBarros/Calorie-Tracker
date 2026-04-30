# Calorie Tracker

Backend para controle de ingestão calórica e acompanhamento nutricional.
A aplicação permite o gerenciamento de usuários, refeições, ingredientes
e metas alimentares, além da geração de relatórios.

## Funcionalidades

- Cadastro e autenticação de usuários com JWT\
- Registro de refeições e ingredientes\
- Cálculo de calorias e macronutrientes\
- Definição de metas nutricionais\
- Geração de relatórios em PDF\
- Execução de tarefas agendadas\
- Controle de banco de dados com migrações (Flyway)

## Tecnologias

- Java 17 ou superior\
- Spring Boot\
- Spring Security\
- JWT (JSON Web Token)\
- Spring Data JPA\
- Flyway\
- Quartz Scheduler\
- Maven\
- PostgreSQL (ou outro banco compatível)

## Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- Java 17 ou superior\
- Maven\
- PostgreSQL\
- Git (opcional)

## Instalação

```bash
git clone <url-do-repositorio>
cd calorie-tracker
```

## Configuração

Edite o arquivo:

    src/main/resources/application.yml

Exemplo:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/calorie_tracker
    username: seu_usuario
    password: sua_senha

  jpa:
    hibernate:
      ddl-auto: validate

  flyway:
    enabled: true
```

## Variáveis de ambiente

```bash
source env.sh
```

## Execução

```bash
mvn spring-boot:run
```

ou

```bash
mvn clean install
java -jar target/*.jar
```

## Autenticação

Header:

    Authorization: Bearer SEU_TOKEN

## Endpoints

### Usuários

- POST /users\
- POST /auth/login

### Refeições

- POST /meals\
- GET /meals

### Ingredientes

- POST /ingredients\
- GET /ingredients

### Metas

- POST /goals\
- GET /diet

## Estrutura

    src/main/java/com/calorietracker

## Testes

```bash
mvn test
```
