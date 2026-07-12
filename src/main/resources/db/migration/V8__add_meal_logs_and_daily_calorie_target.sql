ALTER TABLE diets
  ADD COLUMN daily_calorie_target DECIMAL(19,2) NULL;

ALTER TABLE diets
  ADD CONSTRAINT chk_diets_daily_calorie_target
  CHECK (daily_calorie_target IS NULL OR daily_calorie_target >= 0);

CREATE TABLE meal_logs (
  id_meal_log BINARY(16) NOT NULL,
  user_id BINARY(16) NOT NULL,
  diet_id BINARY(16) NULL,
  consumed_at DATETIME NOT NULL,
  description VARCHAR(255),
  total_calories DECIMAL(19,2) NOT NULL,
  PRIMARY KEY (id_meal_log),
  CONSTRAINT fk_meal_logs_user
    FOREIGN KEY (user_id) REFERENCES users (id_user),
  CONSTRAINT fk_meal_logs_diet
    FOREIGN KEY (diet_id) REFERENCES diets (id_diet)
    ON DELETE SET NULL,
  CONSTRAINT chk_meal_logs_total_calories
    CHECK (total_calories >= 0)
) ENGINE=InnoDB;

CREATE INDEX idx_meal_logs_user_consumed_at
  ON meal_logs(user_id, consumed_at);

CREATE INDEX idx_meal_logs_diet_id
  ON meal_logs(diet_id);

CREATE TABLE meal_log_items (
  id_meal_log_item BINARY(16) NOT NULL,
  meal_log_id BINARY(16) NOT NULL,
  ingredient_id BINARY(16) NOT NULL,
  weight DECIMAL(19,2) NOT NULL,
  calories DECIMAL(19,2) NOT NULL,
  PRIMARY KEY (id_meal_log_item),
  CONSTRAINT fk_meal_log_items_meal_log
    FOREIGN KEY (meal_log_id) REFERENCES meal_logs (id_meal_log)
    ON DELETE CASCADE,
  CONSTRAINT fk_meal_log_items_ingredient
    FOREIGN KEY (ingredient_id) REFERENCES ingredients (id_ingredient)
    ON DELETE RESTRICT,
  CONSTRAINT chk_meal_log_items_weight
    CHECK (weight >= 0),
  CONSTRAINT chk_meal_log_items_calories
    CHECK (calories >= 0)
) ENGINE=InnoDB;

CREATE INDEX idx_meal_log_items_meal_log_id
  ON meal_log_items(meal_log_id);

CREATE INDEX idx_meal_log_items_ingredient_id
  ON meal_log_items(ingredient_id);
