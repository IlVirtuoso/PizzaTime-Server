import { Injectable, numberAttribute } from '@angular/core';
import { IDataBridge } from '../idatabridge';
import { UniqueNamesGenerator } from 'unique-names-generator/dist/unique-names-generator.constructor';
import { Axios } from 'axios';
import { adjectives, colors, uniqueNamesGenerator } from 'unique-names-generator';
import { Pizza } from '@data/Pizza';
import { User } from '@data/User';
import { Pizzeria } from '@data/Pizzeria';


@Injectable({
  providedIn: 'root',
})
export class MockBridgeService extends IDataBridge {

  public override async getPizza(id: string): Promise<Pizza> {
    let name = uniqueNamesGenerator({dictionaries:[colors,adjectives]});
    return new Pizza(id,name,Math.random());
  }



  public override getUser(username: string): Promise<User> {
    throw new Error('Method not implemented.');
  }
  public override getPizzeria(piva: string): Promise<Pizzeria> {
    throw new Error('Method not implemented.');
  }
  public override async login(username: string, password: string): Promise<boolean> {
    return true;
  }




  constructor() {
    super();
  }

}
