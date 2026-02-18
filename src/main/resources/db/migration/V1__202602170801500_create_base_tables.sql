
CREATE TABLE users (
  id_user BINARY(16) NOT NULL,
  name VARCHAR(255),
  weight DECIMAL(19,2),
  height DECIMAL(19,2),
  age BIGINT,
  PRIMARY KEY (id_user)
) ENGINE=InnoDB;

CREATE TABLE goals (
  id_goal BINARY(16) NOT NULL,
  weight DECIMAL(19,2),
  imc DECIMAL(19,2),
  bf DECIMAL(19,2),
  tmb DECIMAL(19,2),
  PRIMARY KEY (id_goal)
) ENGINE=InnoDB;

CREATE TABLE status (
  id_status BINARY(16) NOT NULL,
  weight DECIMAL(19,2) NOT NULL,
  imc DECIMAL(19,2),
  bf DECIMAL(19,2),
  tmb DECIMAL(19,2),
  PRIMARY KEY (id_status)
) ENGINE=InnoDB;

CREATE TABLE macros (
  id_macro BINARY(16) NOT NULL,
  carb DECIMAL(19,2) NOT NULL,
  protein DECIMAL(19,2) NOT NULL,
  fat DECIMAL(19,2) NOT NULL,
  calories DECIMAL(19,2) NOT NULL,
  PRIMARY KEY (id_macro),
  CONSTRAINT chk_macros_carb CHECK (carb >= 0),
  CONSTRAINT chk_macros_protein CHECK (protein >= 0),
  CONSTRAINT chk_macros_fat CHECK (fat >= 0),
  CONSTRAINT chk_macros_calories CHECK (calories >= 0)
) ENGINE=InnoDB;

CREATE TABLE ingredients (
  id_ingredient BINARY(16) NOT NULL,
  name VARCHAR(255),
  macro_id BINARY(16),
  PRIMARY KEY (id_ingredient)
) ENGINE=InnoDB;

CREATE TABLE diets (
  id_diet BINARY(16) NOT NULL,
  name VARCHAR(255),
  total_calories DECIMAL(19,2),
  initial_date DATE,
  final_date DATE,
  PRIMARY KEY (id_diet)
) ENGINE=InnoDB;

CREATE TABLE meals (
  id_meal BINARY(16) NOT NULL,
  meal_date_time DATETIME,
  description VARCHAR(255),
  total_calories_per_meal DECIMAL(19,2),
  diet_id BINARY(16),
  PRIMARY KEY (id_meal)
) ENGINE=InnoDB;

CREATE TABLE meal_ingredients (
  id_meal_ingredient BINARY(16) NOT NULL,
  weight DECIMAL(19,2),
  meal_id BINARY(16) NOT NULL,
  ingredient_id BINARY(16) NOT NULL,
  PRIMARY KEY (id_meal_ingredient)
) ENGINE=InnoDB;