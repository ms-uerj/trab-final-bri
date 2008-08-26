
drop database bri;
create database bri;

USE bri;

--
-- DROP ALL TABLES BEFORE CREATING
--

DROP TABLE IF EXISTS ARTICLE;
DROP TABLE IF EXISTS BOOK;
DROP TABLE IF EXISTS INPROCEEDINGS;
DROP TABLE IF EXISTS INCOLLECTION;
DROP TABLE IF EXISTS MASTERTHESIS;
DROP TABLE IF EXISTS PHDTHESIS;
DROP TABLE IF EXISTS PROCEEDINGS;
DROP TABLE IF EXISTS AUTHOR;
DROP TABLE IF EXISTS ARTICLE_AUTHOR;
DROP TABLE IF EXISTS INPROCEEDINGS_AUTHOR;
DROP TABLE IF EXISTS INCOLLECTION_AUTHOR;

--
-- TABLE DEFINITION FOR USER
--

CREATE TABLE AUTHOR
(
id int unsigned not null auto_increment,
name varchar(255) not null,
primary key (id),
FULLTEXT (name)
) engine=MyISAM;

CREATE TABLE ARTICLE
(
 id int unsigned not null auto_increment,
 title varchar(255),
 journal varchar(255),
 year varchar(30),
 primary key (id),
 link varchar(255),
 FULLTEXT (title,journal,year)
) engine=MyISAM;

CREATE TABLE BOOK
(
 id int unsigned not null auto_increment,
 author_id int unsigned,
 editor varchar(255),
 publisher varchar(255),
 year varchar(30),
 primary key (id),
 foreign key (author_id) references AUTHOR (id),
 FULLTEXT (editor,publisher,year)
) engine=MyISAM;

CREATE TABLE INCOLLECTION
(
 id int unsigned not null auto_increment,
 id_book int unsigned,
 title varchar(255),
 booktitle varchar(255),
 publisher varchar(255),
 year varchar(30),
 content text,
 link varchar(255),
 primary key (id),
 foreign key (id_book) references BOOK (id),
 FULLTEXT (title,booktitle,publisher,year, content)
) engine=MyISAM;

CREATE TABLE PROCEEDINGS
(
 id int unsigned not null auto_increment,
 title varchar(255),
 year varchar(30),
 qualis char(3) default '',
 primary key (id),
 FULLTEXT (title,year)
) engine=MyISAM;

CREATE TABLE INPROCEEDINGS
(
 id int unsigned not null auto_increment,
 id_proceedings varchar(255),
 title varchar(255),
 booktitle varchar(255),
 year varchar(30),
 content text,
 link varchar(255),
 primary key (id),
 foreign key (id_proceedings) references PROCEEDINGS (id),
 FULLTEXT (title,booktitle,year,content)
) engine=MyISAM;

CREATE TABLE MASTERTHESIS
(
 id int unsigned not null auto_increment,
 id_author int unsigned,
 title varchar(255),
 school varchar(255),
 year varchar(30),
 primary key (id),
 foreign key (id_author) references AUTHOR (id),
 FULLTEXT (title,school ,year)
) engine=MyISAM;

CREATE TABLE PHDTHESIS
(
 id int unsigned not null auto_increment,
 id_author int unsigned,
 title varchar(255),
 school varchar(255),
 year varchar(30),
 primary key (id),
 foreign key (id_author) references AUTHOR (id),
 FULLTEXT (title,school ,year)
) engine=MyISAM;

CREATE TABLE ARTICLE_AUTHOR (
 id_article int unsigned not null,
 id_author int unsigned not null,
 position_author int unsigned,
 foreign key (id_article) references T_ARTICLE (id),
 foreign key (id_author) references T_AUTHOR (id)
) engine=MyISAM;

CREATE TABLE INPROCEEDINGS_AUTHOR (
 id_inproceedings int unsigned not null,
 id_author int unsigned not null,
 position_author int unsigned,
 foreign key (id_inproceedings) references T_INPROCEEDINGS (id),
 foreign key (id_author) references T_AUTHOR (id)
) engine=MyISAM;

CREATE TABLE INCOLLECTION_AUTHOR (
 id_incollection int unsigned not null,
 id_author int unsigned not null,
 position_author int unsigned,
 foreign key (id_incollection) references T_INCOLLECTION (id),
 foreign key (id_author) references T_AUTHOR (id)
) engine=MyISAM;
