import { Injectable, numberAttribute } from '@angular/core';
import { IDataBridge } from '../idatabridge';
import {
  adjectives,
  colors,
  uniqueNamesGenerator,
} from 'unique-names-generator';
import { Pizza } from '@data/Pizza';
import { Pizzeria } from '@data/Pizzeria';
import { User } from '@data/User';

class UserAuth {
  public constructor(public user: User, public password: string) {}
}

@Injectable({
  providedIn: 'root',
})
export class MockBridgeService extends IDataBridge {
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

  private authenticated: User | null = null;

  public override getAuthenticatedUser(): User | null {
    return this.authenticated;
  }

  private users: UserAuth[] = [];
  public override signin(username: string, password: string): Promise<boolean> {
    return this.async(() => {
      if (
        this.users.map((user) => user.user.username).lastIndexOf(username) != -1
      ) {
        return false;
      }
      this.users.push(new UserAuth(new User(username), password));
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
    username: string,
    password: string
  ): Promise<boolean> {
    return this.async(() => {
      if (
        this.users.map((user) => user.user.username).lastIndexOf(username) == -1
      ) {
        return false;
      } else {
        let index = this.users
          .map((user) => user.user.username)
          .lastIndexOf(username);
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
