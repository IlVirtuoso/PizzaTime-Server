import { CommonModule, NgFor, NgIf, NgTemplateOutlet } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChange,
  SimpleChanges,
  input,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Order } from '@data/Order';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';

export enum ListMode{
  ListOnly = 'List',
  AcceptReject = 'AcceptReject'
}

@Component({
  selector: 'app-order-list-view',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    NgFor,
    NgIf,
    NgTemplateOutlet,
    ImportsModule
  ],
  templateUrl: './order-list-view.component.html',
  styleUrl: './order-list-view.component.css',
})
export class OrderListViewComponent {
  @Input() orderList : Order[] = [];
  @Input() listMode : ListMode = ListMode.ListOnly;
  @Output() onAccepted: EventEmitter<Order> = new EventEmitter(false);
  @Output() onRejected: EventEmitter<Order> = new EventEmitter(false);


  public accept(order: Order){

    this.onRejected.emit(order);
  }

  public reject(order: Order){
    this.onAccepted.emit(order);
  }




}
