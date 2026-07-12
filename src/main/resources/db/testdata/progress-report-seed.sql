-- Dados de teste para GET /api/reports/progress.
--
-- Requisitos:
--   MySQL 8 ou superior.
--   Migrations Flyway executadas até V8.
--   Banco calorieTracker selecionado como conexão ativa.
--
-- Execução no DBeaver:
--   Abra este arquivo em um editor SQL conectado ao banco calorieTracker.
--   Use "Executar script SQL" (Alt+X ou o botão de script).
--   Não selecione todo o arquivo para executar como uma única instrução.
--
-- Credenciais do usuário criado:
--   E-mail: progress.report@example.com
--   Senha: Test@123
--
-- O cenário contém:
--   Uma dieta vigente com meta diária de 2000 kcal.
--   Duas refeições em cada dia com consumo.
--   Dois dias encerrados sem refeições.
--   Duas janelas de sete dias com tendência de aproximação da meta.
--
-- O script é idempotente para os identificadores de teste abaixo.

-- UNHEX com REPLACE é usado no lugar de UUID_TO_BIN para evitar diferenças
-- entre versões do MySQL e parsers SQL do DBeaver.
SET @user_id = UNHEX(REPLACE('10000000-0000-0000-0000-000000000001', '-', ''));
SET @diet_id = UNHEX(REPLACE('20000000-0000-0000-0000-000000000001', '-', ''));
SET @macro_id = UNHEX(REPLACE('50000000-0000-0000-0000-000000000001', '-', ''));
SET @ingredient_id = UNHEX(REPLACE('60000000-0000-0000-0000-000000000001', '-', ''));

START TRANSACTION;

INSERT INTO users (
  id_user,
  name,
  weight,
  height,
  gender,
  imc,
  tmb,
  birth_date,
  email,
  password
) VALUES (
  @user_id,
  'Usuário Relatório Progresso',
  80000.00,
  175.00,
  'MALE',
  26.12,
  1748.75,
  '1990-01-15',
  'progress.report@example.com',
  '$2y$10$Js8cnpr1UsWXhD5aR5cA6uQe4R4uHIMaGSC8SVXvkp02zRpkpubyW'
)
ON DUPLICATE KEY UPDATE
  name = 'Usuário Relatório Progresso',
  weight = 80000.00,
  height = 175.00,
  gender = 'MALE',
  imc = 26.12,
  tmb = 1748.75,
  birth_date = '1990-01-15',
  email = 'progress.report@example.com',
  password = '$2y$10$Js8cnpr1UsWXhD5aR5cA6uQe4R4uHIMaGSC8SVXvkp02zRpkpubyW';

INSERT INTO macros (
  id_macro,
  carb,
  protein,
  fat,
  calories
) VALUES (
  @macro_id,
  20.00,
  15.00,
  6.00,
  200.00
)
ON DUPLICATE KEY UPDATE
  carb = 20.00,
  protein = 15.00,
  fat = 6.00,
  calories = 200.00;

INSERT INTO ingredients (
  id_ingredient,
  name,
  macro_id
) VALUES (
  @ingredient_id,
  'Refeição balanceada de teste',
  @macro_id
)
ON DUPLICATE KEY UPDATE
  name = 'Refeição balanceada de teste',
  macro_id = @macro_id;

INSERT INTO diets (
  id_diet,
  name,
  total_calories,
  daily_calorie_target,
  initial_date,
  final_date,
  user_id
) VALUES (
  @diet_id,
  'Dieta de teste - 2000 kcal',
  0.00,
  2000.00,
  DATE_SUB(CURDATE(), INTERVAL 90 DAY),
  NULL,
  @user_id
)
ON DUPLICATE KEY UPDATE
  name = 'Dieta de teste - 2000 kcal',
  total_calories = 0.00,
  daily_calorie_target = 2000.00,
  initial_date = DATE_SUB(CURDATE(), INTERVAL 90 DAY),
  final_date = NULL,
  user_id = @user_id;

-- Remove somente o histórico do usuário dedicado ao cenário antes de recriá-lo.
DELETE FROM meal_logs
WHERE user_id = @user_id;

DROP TEMPORARY TABLE IF EXISTS progress_report_seed_days;

CREATE TEMPORARY TABLE progress_report_seed_days (
  day_offset INT NOT NULL PRIMARY KEY,
  daily_calories DECIMAL(19,2) NOT NULL
);

-- Offset zero representa hoje. Os offsets 18 e 23 são omitidos para testar
-- daysWithoutMeals.
INSERT INTO progress_report_seed_days (day_offset, daily_calories) VALUES
  (0, 1900.00),
  (1, 1980.00),
  (2, 2020.00),
  (3, 2010.00),
  (4, 2050.00),
  (5, 1990.00),
  (6, 2040.00),
  (7, 2000.00),
  (8, 2300.00),
  (9, 2250.00),
  (10, 2350.00),
  (11, 2280.00),
  (12, 2320.00),
  (13, 2260.00),
  (14, 2340.00),
  (15, 2150.00),
  (16, 2120.00),
  (17, 2180.00),
  (19, 2080.00),
  (20, 2140.00),
  (21, 2110.00),
  (22, 2160.00),
  (24, 2090.00),
  (25, 2130.00),
  (26, 2070.00),
  (27, 2100.00);

-- Primeira refeição do dia, correspondente a 40 por cento das calorias.
INSERT INTO meal_logs (
  id_meal_log,
  user_id,
  diet_id,
  consumed_at,
  description,
  total_calories
)
SELECT
  UNHEX(REPLACE(
    CONCAT(
      '30000000-0000-0000-0000-',
      LPAD(day_offset * 10 + 1, 12, '0')
    ),
    '-',
    ''
  )),
  @user_id,
  @diet_id,
  CASE
    WHEN day_offset = 0 THEN NOW()
    ELSE TIMESTAMP(
      DATE_SUB(CURDATE(), INTERVAL day_offset DAY),
      '08:00:00'
    )
  END,
  CONCAT('Café da manhã de teste - D-', day_offset),
  daily_calories * 0.40
FROM progress_report_seed_days;

-- Segunda refeição do dia, correspondente a 60 por cento das calorias.
INSERT INTO meal_logs (
  id_meal_log,
  user_id,
  diet_id,
  consumed_at,
  description,
  total_calories
)
SELECT
  UNHEX(REPLACE(
    CONCAT(
      '30000000-0000-0000-0000-',
      LPAD(day_offset * 10 + 2, 12, '0')
    ),
    '-',
    ''
  )),
  @user_id,
  @diet_id,
  CASE
    WHEN day_offset = 0 THEN NOW()
    ELSE TIMESTAMP(
      DATE_SUB(CURDATE(), INTERVAL day_offset DAY),
      '19:00:00'
    )
  END,
  CONCAT('Jantar de teste - D-', day_offset),
  daily_calories * 0.60
FROM progress_report_seed_days;

-- O ingrediente possui 200 kcal por 100 g. Portanto, o peso em gramas é
-- metade das calorias registradas em cada refeição.
INSERT INTO meal_log_items (
  id_meal_log_item,
  meal_log_id,
  ingredient_id,
  weight,
  calories
)
SELECT
  UNHEX(REPLACE(
    CONCAT(
      '40000000-0000-0000-0000-',
      LPAD(day_offset * 10 + 1, 12, '0')
    ),
    '-',
    ''
  )),
  UNHEX(REPLACE(
    CONCAT(
      '30000000-0000-0000-0000-',
      LPAD(day_offset * 10 + 1, 12, '0')
    ),
    '-',
    ''
  )),
  @ingredient_id,
  daily_calories * 0.40 / 2,
  daily_calories * 0.40
FROM progress_report_seed_days;

INSERT INTO meal_log_items (
  id_meal_log_item,
  meal_log_id,
  ingredient_id,
  weight,
  calories
)
SELECT
  UNHEX(REPLACE(
    CONCAT(
      '40000000-0000-0000-0000-',
      LPAD(day_offset * 10 + 2, 12, '0')
    ),
    '-',
    ''
  )),
  UNHEX(REPLACE(
    CONCAT(
      '30000000-0000-0000-0000-',
      LPAD(day_offset * 10 + 2, 12, '0')
    ),
    '-',
    ''
  )),
  @ingredient_id,
  daily_calories * 0.60 / 2,
  daily_calories * 0.60
FROM progress_report_seed_days;

DROP TEMPORARY TABLE progress_report_seed_days;

COMMIT;

-- Verificação resumida dos dados inseridos.
SELECT
  DATE(ml.consumed_at) AS consumption_date,
  COUNT(*) AS meal_count,
  SUM(ml.total_calories) AS consumed_calories
FROM meal_logs ml
WHERE ml.user_id = @user_id
GROUP BY DATE(ml.consumed_at)
ORDER BY consumption_date;
