import { Time } from "@angular/common";
import { Timestamp } from "rxjs";

export enum OrderStatus{
    QUEUED = 0,
    SERVING = 1,
    SERVED = 2,
    CANCELED = 3
}

export class Order{

    public constructor(
        public orderId : String,
        public totalPrice: number,
        public date:EpochTimeStamp,
        public orderStatus: OrderStatus
    ){}
}

