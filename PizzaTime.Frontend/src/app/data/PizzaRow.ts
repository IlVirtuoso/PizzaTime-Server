import { Pizza } from "./Pizza";


export class PizzaRow{

       
    public constructor(
        public rowid: number,
        public cost: number,
        public commonName: string,
        public pizza: Pizza,
    ){ }

}
