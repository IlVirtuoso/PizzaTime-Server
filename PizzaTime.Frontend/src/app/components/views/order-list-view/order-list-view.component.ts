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
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { User } from '@data/User';
import { IDataBridge } from 'app/services/idatabridge';
import { CardModule } from 'primeng/card';
import { TabMenuModule } from 'primeng/tabmenu';
import { MenuItem } from 'primeng/api';
import { DataViewModule } from 'primeng/dataview';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { Order } from '@data/Order';


export enum ListMode{
  ListOnly = 'List',
  AcceptReject = 'AcceptReject'
}

@Component({
  selector: 'app-order-list-view',
  standalone: true,
  imports: [
    CardModule,
    TabMenuModule,
    DataViewModule,
    CommonModule,
    FormsModule,
    NgFor,
    NgIf,
    NgTemplateOutlet,
    InputTextModule,
    FloatLabelModule,
    InputTextModule,
    ButtonModule,
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
