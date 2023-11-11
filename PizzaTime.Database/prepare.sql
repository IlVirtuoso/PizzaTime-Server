CREATE USER "PizzaAdmin" WITH PASSWORD 'Pizza1234!';
CREATE DATABASE "PizzaService";
grant connect on DATABASE "PizzaService" to "PizzaAdmin";
GRANT pg_read_all_data TO "PizzaAdmin";
GRANT pg_write_all_data TO "PizzaAdmin";

\c "PizzaService";

CREATE TABLE Pizza(
    Id SERIAL NOT NULL ,
    Name VARCHAR NOT NULL,
    Description VARCHAR,
    Price FLOAT,
    PRIMARY KEY(Id)
);

CREATE TABLE Users(
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
    FOREIGN KEY (UserName) REFERENCES Users(UserName)
);

CREATE TABLE PizzeriaAuth (
    PIVA VARCHAR not NULL,
    Password VARCHAR not NULL,
    FOREIGN KEY (PIVA) REFERENCES Pizzeria(PIVA)
);



