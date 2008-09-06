
drop database citeseer;
create database citeseer;

USE citeseer;

--
-- DROP ALL TABLES BEFORE CREATING
--

DROP TABLE IF EXISTS PAPER;
DROP TABLE IF EXISTS PAPER_REFERENCES;
DROP TABLE IF EXISTS PAPER_REFERENCES_TEMP;
DROP TABLE IF EXISTS PAPER_IS_REFERENCED_BY;
DROP TABLE IF EXISTS PAPER_IS_REFERENCED_BY_TEMP;


CREATE TABLE PAPER
(
 id int unsigned not null auto_increment,
 title varchar(512),
 primary key (id)
) engine=InnoDB;

CREATE TABLE PAPER_REFERENCES (
 id_article1 int unsigned not null,
 id_article2 int unsigned not null,
 foreign key (id_article1) references PAPER (id),
 foreign key (id_article2) references PAPER (id)
) engine=InnoDB;

CREATE TABLE PAPER_REFERENCES_TEMP (
 id_article1 int unsigned not null,
 id_article2 int unsigned not null
) engine=InnoDB;

CREATE TABLE PAPER_IS_REFERENCED_BY (
 id_article1 int unsigned not null,
 id_article2 int unsigned not null,
 foreign key (id_article1) references PAPER (id),
 foreign key (id_article2) references PAPER (id)
) engine=InnoDB;

CREATE TABLE PAPER_IS_REFERENCED_BY_TEMP (
 id_article1 int unsigned not null,
 id_article2 int unsigned not null
) engine=InnoDB;



