CREATE USER "PizzaAdmin" WITH PASSWORD 'Pizza1234!';
CREATE DATABASE "PizzaService";
GRANT ALL PRIVILEGES ON "PizzaService" TO "PizzaAdmin";
\connect PizzaService;

CREATE TABLE Pizza(
    Id INT NOT NULL AUTO_INCREMENT,
    Name VARCHAR NOT NULL,
    Description VARCHAR,
    Price DOUBLE,
    PRIMARY KEY(Id)
);

CREATE TABLE User(
    UserName VARCHAR NOT NULL,
    Name VARCHAR not NULL,
    SurName VARCHAR not NULL,
    Email VARCHAR not NULL,
    PRIMARY KEY (UserName)
);

CREATE TABLE Pizzeria(
    Email VARCHAR not NULL,
    PIVA VARCHAR not NULL,
    Address VARCHAR not NULL,
    PRIMARY KEY(PIVA)
);

CREATE TABLE UserAuth(
    UserName VARCHAR not NULL,
    Password VARCHAR not NULL,
    FOREIGN KEY (`UserName`) REFERENCES `User`(`UserName`);
);

CREATE TABLE PizzeriaAuth (
    PIVA VARCHAR not NULL,
    Password VARCHAR not NULL,
    FOREIGN KEY (`PIVA`) REFERENCES `Pizzeria`(`PIVA`);
);


