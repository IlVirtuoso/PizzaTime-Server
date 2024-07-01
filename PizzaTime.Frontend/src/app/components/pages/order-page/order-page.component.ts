import { Component } from '@angular/core';
import { Order } from '@data';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';
import { IDataBridge } from 'app/services/idatabridge';

@Component({
  selector: 'app-order-page',
  standalone: true,
  imports: [
    ImportsModule
  ],
  templateUrl: './order-page.component.html',
  styleUrl: './order-page.component.css'
})
export class OrderPageComponent {

  protected currentOrder : Order|  null=  null;
  protected step = "Idle";


  public constructor(private dataservice : IDataBridge){

  }


  async createOrder(){
    this.currentOrder = await this.dataservice.createOrder();
    this.step = "Created";
  }

  async addPizza(pizza : string){
    if (this.currentOrder == null){
      throw new Error("Cannot be null");
    }

  }

  async submit(){
    if(this.currentOrder == null){
      throw new Error("Cannot be null")
    }
    this.dataservice.submitOrder(this.currentOrder);
  }


}
