DROP DATABASE IF EXISTS dbsample;
CREATE DATABASE dbsample CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE dbsample;

DROP TABLE IF EXISTS tbluser;
CREATE TABLE tbluser (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  uname VARCHAR(50) NOT NULL,
  pword VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_tbluser_uname (uname)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS tblcars;
CREATE TABLE tblcars (
  id INT UNSIGNED NOT NULL,
  name VARCHAR(120) NOT NULL,
  year SMALLINT UNSIGNED NOT NULL,
  dailyRate DECIMAL(10,2) NOT NULL,
  isAvailable BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id),
  CHECK (year >= 1900 AND year <= 2100),
  CHECK (dailyRate >= 0)
) ENGINE=InnoDB;

INSERT INTO tbluser (uname, pword) VALUES
  ('admin', 'admin'),
  ('group11', 'group11');

-- Seed cars (sample inventory)
INSERT INTO tblcars (id, name, year, dailyRate, isAvailable) VALUES
  (1, 'Toyota Vios', 2020, 1500.00, TRUE),
  (2, 'Honda City', 2021, 1700.00, TRUE),
  (3, 'Mitsubishi Mirage', 2019, 1200.00, FALSE),
  (4, 'Nissan Almera', 2022, 1800.00, TRUE),
  (5, 'Toyota Innova', 2018, 2200.00, TRUE);
