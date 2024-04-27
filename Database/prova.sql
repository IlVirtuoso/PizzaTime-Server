-- create 

create table accounts(
    id serial,
    name varchar,
    balance numeric,
    PRIMARY KEY (id)
)



-- insert values


insert into accounts(name,balance) values('Alice',100);
insert into accounts(name,balance) values('Bob',50);
insert into accounts(name,balance) values('Carol',90);

-- create store procedure 

CREATE OR REPLACE PROCEDURE transfer(sender int, receiver int, amount numeric)
language plpgsql as $$ 
begin
    update accounts set balance = balance - amount where id = sender;
    update accounts set balance = balance + amount where id = receiver;
    raise notice 'Transfer from % to % of %',sender,receiver,amount;
end;$$;


-- call store procedure

transfer(1,2,50);