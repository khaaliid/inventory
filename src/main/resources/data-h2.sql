CREATE TABLE user (
  user_id int(11) NOT NULL AUTO_INCREMENT,
  email varchar(45) DEFAULT NULL,
  password varchar(200) DEFAULT NULL,
  active int(11) DEFAULT NULL,
  name varchar(45) DEFAULT NULL,
  last_name varchar(45) DEFAULT NULL,
  PRIMARY KEY (user_id)
);

INSERT INTO user (email, password, active, name, last_name) VALUES ('k.elgohare', '$10$zpCRbqW.b4Xi1ozH7DswnuQr6GFkudFCeLpSxsiUHdRuaZFjQvz7K', '1', 'khaled', 'elgohary');