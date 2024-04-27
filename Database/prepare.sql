CREATE USER "PizzaAdmin" WITH PASSWORD 'Pizza1234!';
CREATE DATABASE "PizzaService";
grant connect on DATABASE "PizzaService" to "PizzaAdmin";
GRANT pg_read_all_data TO "PizzaAdmin";
GRANT pg_write_all_data TO "PizzaAdmin";
\c "PizzaService";

GRANT ALL on schema public to "PizzaAdmin";


CREATE TABLE Pizzas(
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

CREATE TABLE Pizzerias(
    Email VARCHAR not NULL,
    PIVA VARCHAR not NULL,
    Address VARCHAR not NULL,
    PRIMARY KEY(PIVA)
);

CREATE TABLE UserAuths(
    UserName VARCHAR not NULL,
    Password VARCHAR not NULL,
    FOREIGN KEY (UserName) REFERENCES Users(UserName)
);

CREATE TABLE PizzeriaAuths (
    PIVA VARCHAR not NULL,
    Password VARCHAR not NULL,
    FOREIGN KEY (PIVA) REFERENCES Pizzerias(PIVA)
);


CREATE TABLE Orders(
    UserName VARCHAR NOT NULL,
    Pizzeria VARCHAR not NULL,
    Pizza INT NOT NULL,
    FOREIGN KEY (UserName) REFERENCES Users(UserName) ,
    FOREIGN KEY (Pizzeria) REFERENCES Pizzerias(PIVA),
    FOREIGN KEY (Pizza) REFERENCES Pizzas(Id)
);

CREATE TABLE Parties(
    PartyName VARCHAR NOT NULL,
    Holder VARCHAR NOT NULL,
    PRIMARY KEY (PartyName),
    FOREIGN KEY (Holder) REFERENCES Users(UserName)
);

CREATE TABLE PartyMemebers(
    UserName VARCHAR NOT NULL,
    PartyName VARCHAR NOT NULL,
    FOREIGN KEY (UserName) REFERENCES Users(UserName),
    FOREIGN KEY (PartyName) REFERENCES Parties(PartyName)
);


