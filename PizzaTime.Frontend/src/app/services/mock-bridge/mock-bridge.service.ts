import { Injectable, numberAttribute } from '@angular/core';
import { IDataBridge } from '../idatabridge';
import {
  adjectives,
  colors,
  uniqueNamesGenerator,
} from 'unique-names-generator';
import {
  Ingredient,
  Order,
  OrderStatus,
  Pizza,
  Pizzeria,
  Role,
  User,
} from '@data';
import { EmailValidator } from '@angular/forms';
import { Observable, ObservableLike, interval, switchAll, switchMap } from 'rxjs';

class UserAuth {
  public constructor(public user: User, public password: string) {}
}

@Injectable({
  providedIn: 'root',
})
export class MockBridgeService extends IDataBridge {

  public override getPizzeriaOrders(piva: string): Observable<Order[]> {
    throw new Error('Method not implemented.');
  }
  public override getOrdersForPizzeria(piva: String): Observable<Order[]> {
    throw new Error('Method not implemented.');
  }
  public override getManagedPizzeria(): Promise<Pizzeria | null> {
    throw new Error('Method not implemented.');
  }

  public override getOrdersForUser(username: String): Observable<Order[]> {
    throw new Error('not implemented');
  }



  public override getAvailableIngredients(): Promise<Ingredient[] | null> {
    return new Promise((t) => {
      t([new Ingredient('1', 'pomodoro', 'salsa di pomodoro', true)]);
    });
  }

  public override getUserBalance(username: string): Promise<number> {
    return this.async(() => Math.random() * 1000);
  }

  private async<T>(f: () => T, timeout: number = 300): Promise<T> {
    return new Promise((t) => {
      t(f());
    });
  }

  public override setUserData(username: string, user: User): Promise<boolean> {
    return this.async(() => {
      return true;
    });
  }

  private authenticated: User | null = {
    address: '',
    email: '',
    name: '',
    phone: '',
    surname: '',
    isVendor: true,
  };

  public override getAuthenticatedUser(): User | null {
    return this.authenticated;
  }

  private users: UserAuth[] = [];
  public override signin(email: string, password: string): Promise<boolean> {
    return this.async(() => {
      if (this.users.map((user) => user.user.email).lastIndexOf(email) != -1) {
        return false;
      }
      this.users.push(new UserAuth(new User(email), password));
      return true;
    });
  }

  public override async getPizza(id: string): Promise<Pizza> {
    let name = uniqueNamesGenerator({ dictionaries: [colors, adjectives] });
    return new Pizza(id, name, Math.random());
  }

  public override getUser(username: string): Promise<User | null> {
    return this.async(() => {
      if (
        this.users.map((user) => user.user.email).lastIndexOf(username) == -1
      ) {
        return null;
      } else {
        let index = this.users
          .map((user) => user.user.email)
          .lastIndexOf(username);
        return this.users[index].user;
      }
    });
  }
  public override getPizzeria(piva: string): Promise<Pizzeria> {
    throw new Error('Method not implemented.');
  }

  public override async login(
    email: string,
    password: string
  ): Promise<boolean> {
    return this.async(() => {
      if (this.users.map((user) => user.user.email).lastIndexOf(email) == -1) {
        return false;
      } else {
        let index = this.users
          .map((user) => user.user.email)
          .lastIndexOf(email);
        if (this.users[index].password != password) {
          return false;
        } else {
          this.authenticated = this.users[index].user;
          return true;
        }
      }
    });
  }

  constructor() {
    super();
  }
}
