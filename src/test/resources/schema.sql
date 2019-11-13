CREATE SCHEMA todo;
USE todo;

DROP TABLE IF EXISTS work;
CREATE TABLE work
(
    id            bigint auto_increment primary key,
    name          varchar(255) null,
    status        varchar(10)  null,
    starting_date datetime     null,
    ending_date   datetime     null
);
