import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Ingredient } from '@data';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';

@Component({
  selector: 'app-ingredient-list-view',
  standalone: true,
  imports: [ImportsModule],
  templateUrl: './ingredient-list-view.component.html',
  styleUrl: './ingredient-list-view.component.css',
})
export class IngredientListViewComponent {
  @Input() public items: Ingredient[] = [];



  @Output() onIngredientSelected: EventEmitter<Ingredient> = new EventEmitter(
    false
  );

  onIngredientTaken(ingredient: Ingredient) {
    this.onIngredientSelected.emit(ingredient);
    console.log(ingredient);
  }
}
