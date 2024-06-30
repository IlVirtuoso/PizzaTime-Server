import { Component, Input } from '@angular/core';
import { Ingredient, Pizza } from '@data';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';

@Component({
  selector: 'app-pizza-menu-adder',
  standalone: true,
  imports: [ImportsModule],
  templateUrl: './pizza-menu-adder.component.html',
  styleUrl: './pizza-menu-adder.component.css',
})
export class PizzaMenuAdderComponent {
  @Input() protected allPizzas: Pizza[] = [];
  @Input() protected selectedPizzas: Pizza[] = [];

  protected examples: Pizza[] = [
    new Pizza('1', 'Margherita', [
      new Ingredient('1', 'Tomato', 'Fresh tomatoes', true, []),
      new Ingredient('2', 'Mozzarella', 'Italian mozzarella cheese', true, [
        'Milk',
      ]),
      new Ingredient('3', 'Basil', 'Fresh basil leaves', true, []),
    ]),
    new Pizza('2', 'Pepperoni', [
      new Ingredient('4', 'Pepperoni', 'Spicy pepperoni slices', true, []),
      new Ingredient('2', 'Mozzarella', 'Italian mozzarella cheese', true, [
        'Milk',
      ]),
      new Ingredient('5', 'Oregano', 'Dried oregano leaves', true, []),
    ]),
    new Pizza('3', 'Vegetarian', [
      new Ingredient('6', 'Bell Peppers', 'Fresh bell peppers', true, []),
      new Ingredient('7', 'Onions', 'Chopped onions', true, []),
      new Ingredient('8', 'Olives', 'Black olives', true, []),
      new Ingredient('2', 'Mozzarella', 'Italian mozzarella cheese', true, [
        'Milk',
      ]),
    ]),
  ];

  selectIngredientsName(pizza: Pizza) : string[]{
    return pizza.seasonings.map(t=> t.commonName);
  }
}
