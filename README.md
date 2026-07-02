# Calorie Tracker

Backend para controle de ingestao calorica e acompanhamento nutricional.
A aplicacao permite gerenciar usuarios, refeicoes, ingredientes, metas,
historico de status nutricional e relatorios em PDF.

## Funcionalidades

- Cadastro e autenticacao de usuarios com JWT
- Registro de refeicoes e ingredientes
- Calculo de calorias e macronutrientes
- Definicao de metas nutricionais
- Acompanhamento de progresso corporal
- Geracao de relatorios em PDF
- Execucao de tarefas agendadas com Quartz e Spring Batch
- Controle de banco de dados com Flyway

## Tecnologias

- Java 25
- Spring Boot 4
- Spring Security
- Spring Data JPA
- Spring Validation
- Spring Batch
- Quartz Scheduler
- Flyway
- MySQL
- Maven
- MapStruct
- Lombok

## Pre-requisitos

- Java 25
- Maven ou Maven Wrapper
- MySQL
- Git

## Configuracao

O projeto usa variaveis de ambiente com valores padrao para banco local:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/calorieTracker
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:root}
```

Variaveis obrigatorias para recursos externos:

```bash
export JWT_SECRET="sua-chave-jwt"
export MAIL_HOST="smtp.example.com"
export MAIL_PORT="587"
export MAIL_USERNAME="usuario"
export MAIL_PASSWORD="senha"
```

## Execucao

```bash
./mvnw spring-boot:run
```

Build:

```bash
./mvnw clean install
java -jar target/*.jar
```

Testes:

```bash
./mvnw test
```

## Autenticacao

Use o token JWT retornado por `/api/auth/login` ou `/api/auth/register`:

```http
Authorization: Bearer SEU_TOKEN
```

## Padrao de DTOs

Os contratos ficam em `src/main/java/com/calorietracker/dtos` e sao separados por papel:

- `request`: entrada HTTP validada com Jakarta Validation, por exemplo `DietRequest`.
- `response`: saida HTTP de endpoints, por exemplo `DietResponse`, `MealResponse`, `ProgressResponse`.
- `summary`: visoes resumidas ou aninhadas, por exemplo `MealSummaryResponse`.
- `report`: estruturas especificas de relatorio, por exemplo `UserReportData`.
- `projections`: interfaces do Spring Data ficam fora de `dtos`, em `projections`.

Controllers nao devem retornar entidades JPA. A camada web fala em `request` e `response`; entidades `*Model` ficam restritas a repositories, services, mappers, batch, security e listeners quando necessario.

## Uso de record

Use `record` para DTOs imutaveis de entrada e saida:

```java
public record DietResponse(
        UUID idDiet,
        String name,
        List<MealResponse> meals,
        BigDecimal totalCalories,
        LocalDate initialDate,
        LocalDate finalDate) {
}
```

Regras do projeto:

- DTOs novos devem ser `record`.
- Nomes devem indicar o papel: `Request`, `Response`, `SummaryResponse` ou `ReportData`.
- Validacoes pertencem aos records de `request`.
- Responses devem expor colecoes como `List`, evitando detalhes internos como `LinkedHashSet`.
- Entidades JPA permanecem classes mutaveis e nao devem ser convertidas para `record`.

## Uso de var

Use `var` apenas em variaveis locais quando o tipo for obvio pelo lado direito:

```java
var user = new UserModel();
var saved = userRepository.save(user);
var created = dietService.create(request);
```

Evite `var` quando ele prejudicar leitura ou esconder regra de negocio:

```java
Optional<DietResponse> result = Optional.empty();
BigDecimal progress = coveredDistance
        .multiply(ONE_HUNDRED)
        .divide(totalDistance, 2, RoundingMode.HALF_UP);
```

Nao use `var` em campos, parametros, retornos de metodos, tipos numericos ambiguos ou em `Optional` usado como estado de fluxo.

## Endpoints Principais

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/users`
- `GET /api/diets`
- `POST /api/diets`
- `GET /api/meals`
- `POST /api/meals`
- `GET /api/status/progress/{userId}`
- `GET /api/reports/users/{userId}/pdf`
