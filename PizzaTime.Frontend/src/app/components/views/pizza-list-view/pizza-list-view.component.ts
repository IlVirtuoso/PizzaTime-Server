import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Pizza } from '@data';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';

@Component({
  selector: 'app-pizza-list-view',
  standalone: true,
  imports: [ImportsModule, CommonModule],
  templateUrl: './pizza-list-view.component.html',
  styleUrl: './pizza-list-view.component.css',
})
export class PizzaListViewComponent {
  @Input() public items: Pizza[] = [];

  @Output() onPizzaSelected: EventEmitter<Pizza> = new EventEmitter(false);


  onPizzaTaken(pizza: Pizza){
    this.onPizzaSelected.emit(pizza);
    console.log(pizza);
  }


}
