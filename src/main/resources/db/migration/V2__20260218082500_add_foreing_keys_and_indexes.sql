ALTER TABLE ingredients
  ADD CONSTRAINT fk_ingredients_macro
  FOREIGN KEY (macro_id) REFERENCES macros (id_macro);

ALTER TABLE ingredients
  ADD UNIQUE KEY uq_ingredients_macro_id (macro_id);

CREATE INDEX idx_ingredients_macro_id ON ingredients(macro_id);

ALTER TABLE meals
  ADD CONSTRAINT fk_meals_diet
  FOREIGN KEY (diet_id) REFERENCES diets (id_diet)
  ON DELETE SET NULL;

CREATE INDEX idx_meals_diet_id ON meals(diet_id);

ALTER TABLE meal_ingredients
  ADD CONSTRAINT fk_meal_ingredients_meal
  FOREIGN KEY (meal_id) REFERENCES meals (id_meal)
  ON DELETE CASCADE;

ALTER TABLE meal_ingredients
  ADD CONSTRAINT fk_meal_ingredients_ingredient
  FOREIGN KEY (ingredient_id) REFERENCES ingredients (id_ingredient)
  ON DELETE RESTRICT;

CREATE INDEX idx_meal_ingredients_meal_id ON meal_ingredients(meal_id);
CREATE INDEX idx_meal_ingredients_ingredient_id ON meal_ingredients(ingredient_id);