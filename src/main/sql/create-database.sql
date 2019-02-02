DROP DATABASE IF EXISTS spring_security_demo_plaintext;
DROP USER IF EXISTS test;


CREATE DATABASE spring_security_demo_plaintext;
\c spring_security_demo_plaintext;

DROP TABLE IF EXISTS users;
CREATE TABLE users (
  username varchar(50) NOT NULL,
  password varchar(70) NOT NULL,
  enabled smallint NOT NULL,
  PRIMARY KEY (username)
);

INSERT INTO users
VALUES
('user','{bcrypt}$2a$04$m9do4mFMUiuj4585ZA5puOVic8AT19/RF58oQbQ3gIzGKZxMAfJsS',1),
('manager','{bcrypt}$2a$04$m9do4mFMUiuj4585ZA5puOVic8AT19/RF58oQbQ3gIzGKZxMAfJsS',1),
('admin','{bcrypt}$2a$04$m9do4mFMUiuj4585ZA5puOVic8AT19/RF58oQbQ3gIzGKZxMAfJsS',1);

--INSERT INTO users
--VALUES
--('user','{noop}123',1),
--('manager','{noop}123',1),
--('admin','{noop}123',1);

DROP TABLE IF EXISTS authorities;
CREATE TABLE authorities (
  username varchar(50) NOT NULL,
  authority varchar(50) NOT NULL,
  CONSTRAINT authorities_idx_1 UNIQUE  (username,authority),
  CONSTRAINT authorities_ibfk_1 FOREIGN KEY (username) REFERENCES users (username)
);

INSERT INTO authorities
VALUES
('user','ROLE_USER'),
('manager','ROLE_MANAGER'),
('admin','ROLE_ADMIN');

CREATE USER test WITH ENCRYPTED PASSWORD 'test';
GRANT ALL PRIVILEGES ON DATABASE spring_security_demo_plaintext TO test;
GRANT ALL PRIVILEGES ON TABLE users TO test;
GRANT ALL PRIVILEGES ON TABLE authorities TO test;