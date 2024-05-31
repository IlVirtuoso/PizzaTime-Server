import { Time } from "@angular/common";
import { Timestamp } from "rxjs";

enum OrderStatus{
    QUEUED = 0,
    SERVING = 1,
    SERVED = 2,
    CANCELED = 3
}

export class Order{
    
    public constructor(
        partyId : String,
        partyLink: String,
        totalPrice: number,
        date:EpochTimeStamp,
        orderStatus: OrderStatus
    ){}
}
