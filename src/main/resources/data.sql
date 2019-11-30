DROP TABLE IF EXISTS user;

CREATE TABLE user (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  nome VARCHAR(250) NOT NULL,
  email VARCHAR(250) NOT NULL
);

INSERT INTO user (nome, email) VALUES
  ('Milton Jacomini', 'milton@meetime.com'),
  ('Bill Gates', 'billgates@microsoft.com'),
  ('Diego Wagner', 'diegowagner@meetime.com');