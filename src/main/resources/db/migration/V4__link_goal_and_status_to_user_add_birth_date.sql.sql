ALTER TABLE users
DROP COLUMN age;

ALTER TABLE users
ADD COLUMN birth_date DATE NULL;

ALTER TABLE goals
  ADD COLUMN user_id BINARY(16) NOT NULL,
  ADD COLUMN start_weight DECIMAL(19,2) NULL,
  ADD COLUMN start_bf DECIMAL(19,2) NULL,
  ADD CONSTRAINT fk_goals_user
    FOREIGN KEY (user_id) REFERENCES users (id_user),
  ADD CONSTRAINT uq_goals_user UNIQUE (user_id);

ALTER TABLE status
  ADD COLUMN user_id BINARY(16) NOT NULL,
  ADD COLUMN created_at DATETIME NULL,
  ADD CONSTRAINT fk_status_user
    FOREIGN KEY (user_id) REFERENCES users (id_user);

CREATE INDEX idx_status_user_id ON status(user_id);
CREATE INDEX idx_status_created_at ON status(created_at);