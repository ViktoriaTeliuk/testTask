DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;
DROP TABLE IF EXISTS files;

/*ALTER SEQUENCE global_seq RESTART WITH 100000;*/

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
  id         INTEGER   DEFAULT nextval('global_seq') PRIMARY KEY,
  name       VARCHAR                 NOT NULL,
  email      VARCHAR                 NOT NULL,
  password   VARCHAR                 NOT NULL,
  registered TIMESTAMP DEFAULT now() NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE files
(
  id           INTEGER DEFAULT nextval('global_seq') PRIMARY KEY,
  user_id      INTEGER   NOT NULL,
  name         VARCHAR   NOT NULL,
  path         VARCHAR   NOT NULL,
  created      TIMESTAMP NOT NULL,
  size         LONG,
  content_type VARCHAR,
  CONSTRAINT user_file_idx UNIQUE (user_id, name),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
