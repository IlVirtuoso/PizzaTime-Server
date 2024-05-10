import { Ingredient } from "./ingredient";

export class Pizza {
  constructor(
    public id: number,
    public name: string,
    public price: number,
    public imageUrl: string,
    public description: string,
    public categoryId: number,
    public categoryName: string,
    public ingredients: Ingredient[]
  ) { }

}
