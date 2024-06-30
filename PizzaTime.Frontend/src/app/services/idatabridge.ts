import { Injectable } from "@angular/core";
//Role doesn't exist
import { Ingredient, Order, Pizza, Pizzeria, Menu, User } from "@data";
import { Observable } from "rxjs";



export class AddPizzaRequest{
  public constructor(
      public cost: number,
      public commonName: string,
      public pizzaId: number
  ){ }
}

export class AddIngrRequest{
  public constructor(
      public cost: number,
      public commonName: string,
      public addition: number
  ){ }
}

export class getMenuRowsForOrderRequest{
  public constructor(
    public pizzaId : number,
    public baseId : number,
    public  additions : Array<number>
  ){}
}


export abstract class IDataBridge {
  lastError: string = '';

  regModeOnly = false;

  //IDP methods
  public abstract login(username: string, password: string): Promise<number>;
  public abstract socialLogin(): Promise<number>;

  public abstract signin(
    firstName: string,
    lastName: string,
    username: string,
    password: string
  ): Promise<number>;
  public abstract finalizeRegistration(
    firstName: string,
    lastName: string,
    address: string,
    phone: string,
    mobile: string
  ): Promise<number>;

  public abstract getJWT(): Promise<boolean>;
  public abstract getUser(): Promise<User | null>;
  public abstract deleteUser(): Promise<boolean>;
  public abstract getUserBalance(): Promise<number>;
  public abstract setUserData(
    firstName: string,
    lastName: string,
    address: string,
    phone: string,
    mobile: string
  ): Promise<boolean>;
  public abstract changePassword(
    oldPassword:string, newPassword:string
  ): Promise<boolean>;
  // You already have a getUser, what do you want here?
  // public abstract getAuthenticatedUser() : User |  null;

  //This method is not secure and it is allowed only in debug mode from idp
  //public abstract getPizzeria(piva: string): Promise<Pizzeria | null>;
  public abstract getManagedPizzeria(): Promise<Pizzeria | null>;
  public abstract openPizzeria(): Promise<boolean>;
  public abstract closePizzeria(): Promise<boolean>;
  public abstract addPizzaToMenu(pizzas: AddPizzaRequest[]): Promise<boolean>;
  public abstract addAdditionToMenu(additions: AddIngrRequest[]): Promise<boolean>;
  public abstract getMenu(): Promise<Menu | null>;
  public abstract createMenu(): Promise<boolean>;
  public abstract getMenuRowsForOrder(order:{order: getMenuRowsForOrderRequest[]}): Observable<Menu[]>;
  public abstract createPizzeria(
    name: string,
    vatNumber: string,
    address: string
  ) : Promise<boolean>;
  public abstract recharge(value:number): Promise<boolean>

  public abstract getAvailableIngredients(): Observable<Ingredient[]>;
  public abstract getAvailablePastry(): Observable<Ingredient[]>;
  public abstract getAvailableSeasoning(): Observable<Ingredient[]>;
  public abstract getAvailablePizza(): Observable<Pizza[]>;

  //You should use a JWT instead of PIVA, we can add the PIVA inside the body if you want
  public abstract getPizzeriaOrders(piva: string): Observable<Order[]>;
  public abstract getOrdersForPizzeria(piva: String): Observable<Order[]>;
  public abstract getOrdersForUser(username: String): Observable<Order[]>;
}
