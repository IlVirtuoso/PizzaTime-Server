import { Ingredient } from "./Ingredient";


export class IngrRow{

    public constructor(
        public rowid: number,
        public cost: number,
        public commonName: string,
        public ingredient: Ingredient,
    ){ }

}
