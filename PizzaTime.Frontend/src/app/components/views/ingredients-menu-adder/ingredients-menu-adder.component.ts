import { Component, Input, Output } from '@angular/core';
import { Ingredient } from '@data';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';

@Component({
  selector: 'app-ingredients-menu-adder',
  standalone: true,
  imports: [ImportsModule],
  templateUrl: './ingredients-menu-adder.component.html',
  styleUrl: './ingredients-menu-adder.component.css',
})
export class IngredientsMenuAdderComponent {
  @Input() public availableIngredients: Ingredient[] = [];
  @Output() public selectedIngredients: Ingredient[] = [];

  protected example: Ingredient[] = [
    new Ingredient('6', 'Bell Peppers', 'Fresh bell peppers', true, []),
    new Ingredient('7', 'Onions', 'Chopped onions', true, []),
    new Ingredient('8', 'Olives', 'Black olives', true, []),
    new Ingredient('2', 'Mozzarella', 'Italian mozzarella cheese', true, [
      'Milk',
    ]),
  ];
}
