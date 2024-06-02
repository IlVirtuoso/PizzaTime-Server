import { CommonModule, NgFor, NgIf, NgTemplateOutlet } from '@angular/common';
import { Component, OnChanges, OnInit, SimpleChange, SimpleChanges } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { User } from '@data/User';
import { IDataBridge } from 'app/services/idatabridge';
import { CardModule } from 'primeng/card';
import { TabMenuModule } from 'primeng/tabmenu';
import { MenuItem } from 'primeng/api';
import { DataViewModule } from 'primeng/dataview';
import { UserField } from '../../views/user-profile-view/userfield';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { UserProfileViewComponent } from 'app/components/views/user-profile-view/user-profile-view.component';
import { ListMode, OrderListViewComponent } from 'app/components/views/order-list-view/order-list-view.component';
import { Order, OrderStatus } from '@data/Order';

@Component({
  selector: 'app-user-home',
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
    OrderListViewComponent,
    ButtonModule,
    UserProfileViewComponent
  ],
  templateUrl: './user-home.component.html',
  styleUrl: './user-home.component.css',
})
export class UserHomeComponent implements OnInit {
  private _user: User | undefined = undefined;

  protected get user() {
    return this._user;
  }

  protected menu_items: MenuItem[] = [
    { label: 'Profile', icon: 'pi pi-home' },
    { label: 'Orders', icon: 'pi pi-shopping-cart' },
  ];



  protected active_item = this.menu_items[1];

  protected orderList : Order[] = [];
  protected mode : ListMode = ListMode.AcceptReject;

  public onTabChange(event: any) {
    this.active_item = event;
  }



  public constructor(private router: Router, private bridge: IDataBridge) {}
  ngOnInit(): void {
    this._user = new User('test', 'test@test.com', 'test', 'test');
    this.orderList = [
      {orderId: '12341-123', date:Date.now(), orderStatus: OrderStatus.SERVING, totalPrice:10.0},
    ]
  }



}
