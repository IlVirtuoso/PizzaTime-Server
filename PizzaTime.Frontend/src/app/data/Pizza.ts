import { Ingredient } from "./Ingredient";

export class Pizza {
  constructor(
    public id: string,
    public commonName: string,
    public seasonings: Ingredient[]
  ) { }
}

