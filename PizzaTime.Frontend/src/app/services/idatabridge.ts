import { Injectable } from "@angular/core";
import { Ingredient } from "@data/Ingredient";
import { Order } from "@data/Order";
import { Pizza } from "@data/Pizza";
import { Pizzeria } from "@data/Pizzeria";
import { User } from "@data/User";

export abstract class IDataBridge {
  lastError: string = "";
  public abstract login(username : string, password: string): Promise<boolean>;
  public abstract signin(username: string, password : string) : Promise<boolean>;
  public abstract getUser(username : string): Promise<User| null>;
  public abstract getPizzeria(piva: string): Promise<Pizzeria | null>;
  public abstract getPizza(id: string) : Promise<Pizza | null>;
  public abstract getAuthenticatedUser() : User |  null;
  public abstract setUserData(username : string, user:User) : Promise<boolean>;
  public abstract getUserBalance(username:string) : Promise<number>;
  public abstract registerPizzeria(pizzeria: Pizzeria): Promise<boolean>;
  public abstract addPizzeriaWorker(piva : String, username: String) : Promise<boolean>;
  public abstract getPizzeriaWorkers(piva: string): Promise<User[] | null>;
  public abstract getPizzeriaOrders(piva: string): Promise<Order[] | null>;
  public abstract getAvailableIngredients(address: String, distance : number) : Promise<Ingredient[] | null>;

}
