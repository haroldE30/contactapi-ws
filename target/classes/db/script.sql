-- create database contact_db
DROP SCHEMA IF EXISTS `contact_db`;
CREATE SCHEMA `contact_db`;

-- select the database
USE `contact_db` ;

-- drop tables
DROP TABLE IF EXISTS `contact`;
DROP TABLE IF EXISTS `address`;
DROP TABLE IF EXISTS `communication`;

CREATE TABLE `contact` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) DEFAULT NOT NULL,
  `last_name` varchar(100) DEFAULT NOT NULL,
  `dob` date DEFAULT NOT NULL,
  `gender` varchar(1) DEFAULT NOT NULL,
  `title` varchar(100) DEFAULT NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `contact` (`id`, `dob`, `first_name`, `gender`, `last_name`, `title`)
VALUES
	(1,'1955-01-18','Kevin','M','Costner','Actor'),
	(2,'1998-11-26','Megan','F','Fox','Actress'),
	(3,'1986-04-22','Amber','F','Heard','Actress'),
	(4,'1992-05-31','Anna-maria','F','Sieklucka','Actress'),
	(5,'1963-06-09','Johnny','M','Depp','Actor');
	

CREATE TABLE `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(10) DEFAULT NOT NULL,
  `number` varchar(10) DEFAULT NOT NULL,
  `unit` varchar(10) DEFAULT NULL,
  `street` varchar(255) DEFAULT NOT NULL,
  `city` varchar(100) DEFAULT NOT NULL,
  `zip_code` varchar(6) DEFAULT NOT NULL,
  `state` varchar(2) DEFAULT NOT NULL,
  `contact_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`)
);

INSERT INTO `address` (`id`, `city`, `number`, `state`, `street`, `type`, `unit`, `zip_code`, `contact_id`)
VALUES
	(1,'Lynwood','65','CA','Norton Road','home','1','90280',1),
	(3,'california','123423','CA','diamond st beverly hills','home','312','90210',2),
	(4,'Texas','542','TX','Austin','home','312','73301',3),
	(5,'Lublin','365','CA','Silver hills','home','6','90210',4),
	(6,'Owensboro','777','KY','Carter Road','home','5','42301',5),
	(7,'Los Angeles','6824','CA','Hollywood Blvd','work','69','90028',5);


CREATE TABLE `communication` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(10) DEFAULT NOT NULL,
  `value` varchar(50) DEFAULT NOT NULL,
  `preferred` tinyint(1) DEFAULT 0,
  `contact_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`)
);

INSERT INTO `communication` (`id`, `preferred`, `type`, `value`, `contact_id`)
VALUES
	(1,0,'email','kevin.costner@gmail.com',1),
	(3,0,'email','megan@fox.com',2),
	(4,0,'mobile','91-633-67890',2),
	(5,0,'email','amber.heard@hotmail.com',3),
	(6,0,'email','am.sieklucka@gmail.com',4),
	(7,0,'mobile','91-888-55555',4),
	(8,0,'email','Johnny.Depp@msn.com',5);