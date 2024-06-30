import { PizzaRow } from "./PizzaRow";
import { IngrRow } from "./IngrRow";


export class Menu{

    public constructor(
        public id: number,
        public pizzeriaId: number,
        public pizzaRows: PizzaRow[],
        public ingrRows: IngrRow[],
    ){ }

}
