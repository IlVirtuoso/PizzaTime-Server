import { IngrRow } from "./IngrRow";
import { PizzaRow } from "./PizzaRow";


export class Menu{

    public constructor(
        public id: number,
        public pizzeriaId: number,
        public pizzaRows: PizzaRow[],
        public ingrRows: IngrRow[],
    ){ }




}
