import { Injectable } from "@angular/core";
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
}
