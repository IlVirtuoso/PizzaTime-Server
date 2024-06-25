import { Injectable } from "@angular/core";
import { Ingredient, Order, Pizza, Pizzeria, Role, User } from "@data";
import { Observable } from "rxjs";

export abstract class IDataBridge {
  lastError: string = "";
  public abstract login(username : string, password: string): Promise<boolean>;
  public abstract signin(username: string, password : string) : Promise<boolean>;
  public abstract getUser(username : string): Promise<User| null>;
  public abstract getPizzeria(piva: string): Promise<Pizzeria | null>;
  public abstract getManagedPizzeria() : Promise<Pizzeria| null>;
  public abstract getPizza(id: string) : Promise<Pizza | null>;
  public abstract getAuthenticatedUser() : User |  null;
  public abstract setUserData(username : string, user:User) : Promise<boolean>;
  public abstract getUserBalance(username:string) : Promise<number>;
  public abstract getPizzeriaOrders(piva: string): Observable<Order[]>;
  public abstract getAvailableIngredients() : Promise<Ingredient[] | null>;
  public abstract getOrdersForUser(username: String) : Observable<Order[]>;
  public abstract getOrdersForPizzeria(piva: String) : Observable<Order[]>;
}
