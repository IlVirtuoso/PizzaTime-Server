import { Injectable } from "@angular/core";
//Role doesn't exist
import { Ingredient, Order, Pizza, Pizzeria, Menu, User } from "@data";
import { Observable } from "rxjs";

export abstract class IDataBridge {
  lastError: string = "";

  //IDP methods 
  public abstract login(username : string, password: string): Promise<boolean>;
  public abstract socialLogin(): Promise<boolean>;
  
  public abstract signin(firstName:string, lastName:string, username: string, password : string) : Promise<boolean>;
  public abstract finalizeRegistration(username: string, password : string) : Promise<boolean>;
  
  public abstract getJWT():Promise<Boolean>;
  public abstract getUser(): Promise<User| null>;
  public abstract deleteUser(): Promise<Boolean>;
  public abstract getUserBalance() : Promise<number>;
  public abstract setUserData(firstName:string, lastName:string, address: string, phone : string, mobile:string) : Promise<boolean>;
  // You already have a getUser, what do you want here?
  // public abstract getAuthenticatedUser() : User |  null;
  

  //This method is not secure and it is allowed only in debug mode from idp
  //public abstract getPizzeria(piva: string): Promise<Pizzeria | null>;
  public abstract getManagedPizzeria() : Promise<Pizzeria| null>;
  public abstract openPizzeria() : Promise<Boolean>;
  public abstract closePizzeria() : Promise<Boolean>;
  public abstract addPizzaToMenu(pizzas:Pizza[]) : Promise<Boolean>;
  public abstract addAdditionToMenu(additions:Ingredient[]) : Promise<Boolean>;
  public abstract getMenu() : Promise<Menu | null>;
  public abstract createMenu() : Promise<Boolean>;
  public abstract getMenuRowsForOrder(order:Order[]) : Promise<Boolean>;
  

  public abstract getAvailableIngredients() : Observable<Ingredient[]>;
  public abstract getAvailablePastry() : Observable<Ingredient[]>;
  public abstract getAvailableSeasoning() : Observable<Ingredient[]>;
  public abstract getAvailablePizza() : Observable<Pizza[]>;


  //You should use a JWT instead of PIVA, we can add the PIVA inside the body if you want
  public abstract getPizzeriaOrders(piva: string): Observable<Order[]>;
  public abstract getOrdersForPizzeria(piva: String) : Observable<Order[]>;
  public abstract getOrdersForUser(username: String) : Observable<Order[]>;
}
