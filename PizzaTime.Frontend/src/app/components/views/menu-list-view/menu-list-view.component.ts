import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Menu } from '@data';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';

@Component({
  selector: 'app-menu-list-view',
  standalone: true,
  imports: [
    ImportsModule
  ],
  templateUrl: './menu-list-view.component.html',
  styleUrl: './menu-list-view.component.css'
})
export class MenuListViewComponent {

  @Input() menuList : Menu[] =[];
  @Output() onItemSelected = new EventEmitter<Menu>();

}

