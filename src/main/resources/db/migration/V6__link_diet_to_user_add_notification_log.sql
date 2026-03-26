ALTER TABLE diets
  ADD COLUMN user_id BINARY(16) NULL;

ALTER TABLE diets
  ADD CONSTRAINT fk_diets_user
    FOREIGN KEY (user_id) REFERENCES users (id_user);

CREATE INDEX idx_diets_user_id ON diets(user_id);

CREATE TABLE notification_logs (
  id_notification_log BINARY(16) NOT NULL,
  user_id BINARY(16) NOT NULL,
  type VARCHAR(100) NOT NULL,
  sent_at DATETIME NOT NULL,
  PRIMARY KEY (id_notification_log),
  CONSTRAINT fk_notification_logs_user
    FOREIGN KEY (user_id) REFERENCES users (id_user)
);

CREATE INDEX idx_notification_logs_user_id ON notification_logs(user_id);
CREATE INDEX idx_notification_logs_type ON notification_logs(type);
CREATE INDEX idx_notification_logs_sent_at ON notification_logs(sent_at);