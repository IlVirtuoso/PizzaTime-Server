import { CommonModule, NgFor, NgIf, NgTemplateOutlet } from '@angular/common';
import { Component, OnChanges, OnInit, SimpleChange, SimpleChanges } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { User } from '@data';
import { IDataBridge } from 'app/services/idatabridge';
import { CardModule } from 'primeng/card';
import { TabMenuModule } from 'primeng/tabmenu';
import { MenuItem } from 'primeng/api';
import { DataViewModule } from 'primeng/dataview';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { UserProfileViewComponent } from 'app/components/views/user-profile-view/user-profile-view.component';
import { ListMode, OrderListViewComponent } from 'app/components/views/order-list-view/order-list-view.component';
import { Order, OrderStatus } from '@data';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';
import { PizzaListViewComponent } from 'app/components/views/pizza-list-view/pizza-list-view.component';
import { Pizza } from '@data';
import { UserListViewComponent } from 'app/components/views/user-list-view/user-list-view.component';
import { CookieService } from 'ngx-cookie-service';

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
    UserProfileViewComponent,
    ImportsModule,
    PizzaListViewComponent,
    UserListViewComponent
  ],
  templateUrl: './user-home.component.html',
  styleUrl: './user-home.component.css',
})
export class UserHomeComponent implements OnInit {
  private _user: User | null = null;

  protected get user() : User| null {
    return this._user;
  }

  protected menu_items: MenuItem[] = [
    { label: 'Profile', icon: 'pi pi-home' },
    { label: 'Orders', icon: 'pi pi-shopping-cart' , disabled:!this.bridge.regModeOnly},
    {label: 'Pizzas', icon: 'pi pi-heart-fill', disabled:!this.bridge.regModeOnly},
    {label: 'Friends', icon:'pi pi-user', disabled:!this.bridge.regModeOnly}
  ];



  protected active_item = this.menu_items[0];

  protected orderList : Order[] = [];
  protected mode : ListMode = ListMode.AcceptReject;

  protected userPizzas : Pizza[]= [

  ];


  protected friends : User[]= [

  ];

  public onTabChange(event: any) {
    this.active_item = event;
  }



  public constructor(private router: Router, private bridge: IDataBridge, private cookieService: CookieService) {}
  async ngOnInit(): Promise<void> {
    if(this.bridge.regModeOnly){
      this._user = this.cookieService.get("Account") as unknown as User;
      console.log(this._user)
    }
    else{
    this._user = await this.bridge.getUser();
    }
  }



}
