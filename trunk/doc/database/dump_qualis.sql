drop database bri;
create database bri;

USE bri;

--
-- DROP ALL TABLES BEFORE CREATING
--

DROP TABLE IF EXISTS QUALIS;

--
-- TABLE DEFINITION FOR USER
--


CREATE TABLE QUALIS
(
id int unsigned not null auto_increment,
issn text,
titulo text,
nivel text,
circulacao text,
primary key (id)
);