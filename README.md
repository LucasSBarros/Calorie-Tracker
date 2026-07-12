# Calorie Tracker

Backend para controle de ingestao calorica e acompanhamento nutricional.
A aplicacao permite gerenciar usuarios, refeicoes, ingredientes, metas,
historico de status nutricional e relatorios em PDF.

Documentação técnica e roteiro de apresentação do relatório de progresso:

- [Relatório de Progresso Calórico](docs/RELATORIO_PROGRESSO.md)

## Funcionalidades

- Cadastro e autenticacao de usuarios com JWT
- Registro de refeicoes e ingredientes
- Registro historico de refeicoes consumidas
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
- `projections`: records e interfaces de leitura do Spring Data ficam fora de `dtos`, em `projections`.

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
- `POST /api/meal-logs`
- `GET /api/meal-logs?from=2026-07-01&to=2026-07-09`
- `GET /api/status/progress/{userId}`
- `GET /api/reports/progress?from=2026-06-12&to=2026-07-09`
- `GET /api/reports/progress/pdf?from=2026-06-12&to=2026-07-09`
- `GET /api/reports/users/{userId}/pdf`

`/api/meals` representa as refeicoes planejadas de uma dieta.
`/api/meal-logs` registra o consumo real do usuario autenticado.
O relatorio de progresso usa os registros de consumo e assume os ultimos 28 dias
quando `from` e `to` nao sao informados. O mesmo relatorio pode ser obtido em
JSON por `/api/reports/progress` ou em PDF por `/api/reports/progress/pdf`.

Dados para testar o relatório podem ser carregados manualmente com:

```bash
mysql -u root -p calorieTracker \
  < src/main/resources/db/testdata/progress-report-seed.sql
```

No DBeaver, abra o arquivo conectado ao banco `calorieTracker` e use
**Executar script SQL** (`Alt+X`). Não envie o conteúdo inteiro como uma única
instrução SQL.

O script cria o usuário `progress.report@example.com` com a senha `Test@123`.
