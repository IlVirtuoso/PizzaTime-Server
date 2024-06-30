import { CommonModule, NgFor, NgIf, NgTemplateOutlet } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Order, Pizzeria, User } from '@data';
import { OrderListViewComponent } from 'app/components/views/order-list-view/order-list-view.component';
import { PizzaListViewComponent } from 'app/components/views/pizza-list-view/pizza-list-view.component';
import { UserListViewComponent } from 'app/components/views/user-list-view/user-list-view.component';
import { UserProfileViewComponent } from 'app/components/views/user-profile-view/user-profile-view.component';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';
import { IDataBridge } from 'app/services/idatabridge';
import { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DataViewModule } from 'primeng/dataview';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { TabMenuModule } from 'primeng/tabmenu';
import { interval } from 'rxjs';
import { MenuPanelComponent } from './menu-panel/menu-panel.component';
import { OrderPanelComponent } from './order-panel/order-panel.component';

@Component({
  selector: 'app-pizzeria-admin-page',
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
    UserListViewComponent,
    MenuPanelComponent,
    OrderPanelComponent
  ],
  templateUrl: './pizzeria-admin-page.component.html',
  styleUrl: './pizzeria-admin-page.component.css',
})
export class PizzeriaAdminPageComponent {
  private _user: User | null = null;

  protected get user(): User | null {
    return this._user;
  }
  protected selected_order: Order | null = null;
  protected pizzeria: Pizzeria | null = null;

  protected menu_items: MenuItem[] = [
    { label: 'Orders', icon: 'pi pi-shopping-cart' },
    { label: 'Menu', icon: 'pi pi-menu' },

  ];

  protected active_item = this.menu_items[0];


  public onTabChange(event: any) {
    this.active_item = event;
  }

  public constructor(private router: Router, private bridge: IDataBridge) {}

  async ngOnInit() {
    this._user = await this.bridge.getUser();
    this.pizzeria = await this.bridge.getManagedPizzeria();
  }
}
