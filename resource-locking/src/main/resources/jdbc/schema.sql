CREATE TABLE resource_lock (
  resource_name   text                        NOT NULL,
  lock_name       text                        NOT NULL,
  created_at      TIMESTAMP WITH TIME ZONE    NOT NULL,
  expires_at      TIMESTAMP WITH TIME ZONE,
  PRIMARY KEY (resource_name)
);