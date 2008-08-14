
--drop database bri;
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

--
-- TABLE DEFINITION FOR USER
--

CREATE TABLE ARTICLE
(
 key text,
 author_id int unsigned,
 title text,
 journal text,
 year text,
 primary key (key),
 foreign key (query_id) references AUTHOR (id),
 FULLTEXT (title,journal,year)
) engine=MyISAM;

CREATE TABLE BOOK
(
 key text,
 author_id int unsigned,
 editor text,
 publisher text,
 year text,
 primary key (key),
 foreign key (query_id) references AUTHOR (id),
 FULLTEXT (editor, title,publisher ,year)
) engine=MyISAM;

CREATE TABLE INPROCEDDINGS
(
 key text,
 author_id int unsigned,
 title text,
 booktitle text,
 year text,
 primary key (key),
 foreign key (query_id) references AUTHOR (id),
 FULLTEXT (title,booktitle,year)
) engine=MyISAM;

CREATE TABLE INCOLLECTION
(
 key text,
 author_id int unsigned,
 title text,
 booktitle text,
 publisher text,
 year text,
 primary key (key),
 foreign key (query_id) references AUTHOR (id),
 FULLTEXT (title,booktitle,publisher,year)
) engine=MyISAM;

CREATE TABLE MASTERTHESIS
(
 key text,
 author_id int unsigned,
 title text,
 school text,
 year text,
 primary key (key),
 foreign key (query_id) references AUTHOR (id),
 FULLTEXT (title,school ,year)
) engine=MyISAM;

CREATE TABLE PHDTHESIS
(
 key text,
 author_id int unsigned,
 title text,
 school text,
 year text,
 primary key (key),
 foreign key (query_id) references AUTHOR (id),
 FULLTEXT (title,school ,year)
) engine=MyISAM;

CREATE TABLE PROCEEDINGS
(
 key text,
 title text,
 year text,
 primary key (key),
 FULLTEXT (title,year)
) engine=MyISAM;

CREATE TABLE AUTHOR
(
id int unsigned not null auto_increment,
name text,
primary key (id),
FULLTEXT (author)
); engine=MyISAM;

