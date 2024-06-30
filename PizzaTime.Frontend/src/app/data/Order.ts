import { Time } from "@angular/common";
import { Timestamp } from "rxjs";

export enum OrderStatus{
    READY = "ready",
    QUEUED = "queued",
    REFUSED = "refused",
    ACCEPTED = "accepted",
    SERVING = "serving",
    COMPLETED = "completed" ,
    CANCELED = "canceled"
}

export class Order{

    public constructor(
        public id : String,
        public totalPrice: number,
        public date:Date,
        public userId : number,
        public pizzeriaId: String,
        public orderStatus: OrderStatus,
        public orderRows: Array<OrderRow>
    ){}
}


export class OrderRow{
  public constructor(
    public pizzaId : number,
    public baseId : number,
    public  ingredients : Array<number>,
    public quantity : number,
  ){}
}


