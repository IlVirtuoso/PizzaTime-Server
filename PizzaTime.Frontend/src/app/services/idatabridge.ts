import { Injectable } from "@angular/core";
import { Ingredient, Order, Pizza, Pizzeria, Role, User } from "@data";

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
  public abstract getAvailableIngredients() : Promise<Ingredient[] | null>;
  public abstract getOrdersForUser(username: String) : Promise<Order[]|null>;
  public abstract getOrdersForPizzeria(piva: String) : Promise<Order[]| null>;
  public abstract validatePizza(pizza: Pizza) : Promise<boolean>;
  public abstract getRolesForUser() : Promise<Role[]| null>;
}
