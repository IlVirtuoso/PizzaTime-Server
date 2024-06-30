import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Ingredient, Menu, Pizza } from '@data';
import { IngredientListViewComponent } from 'app/components/views/ingredient-list-view/ingredient-list-view.component';
import { IngredientsMenuAdderComponent } from 'app/components/views/ingredients-menu-adder/ingredients-menu-adder.component';
import { PizzaListViewComponent } from 'app/components/views/pizza-list-view/pizza-list-view.component';
import { PizzaMenuAdderComponent } from 'app/components/views/pizza-menu-adder/pizza-menu-adder.component';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';
import { AddIngrRequest, IDataBridge } from 'app/services/idatabridge';

@Component({
  selector: 'app-menu-panel',
  standalone: true,
  imports: [
    ImportsModule,
    CommonModule,
    PizzaListViewComponent,
    IngredientListViewComponent,
    IngredientsMenuAdderComponent,
    PizzaMenuAdderComponent,
  ],
  templateUrl: './menu-panel.component.html',
  styleUrl: './menu-panel.component.css'
})
export class MenuPanelComponent implements OnInit{

  protected mode = "View";
  protected menu: Menu | null = null;
  protected pizzas : Pizza[] = [];
  protected ingredients : Ingredient[] = [];



  protected submittedIngredients : Ingredient[] = [];
  protected submittedPizzas : Pizza[] = [];




  constructor(private dataservice: IDataBridge) {

   }


  apply(): void{

  }

  async ngOnInit(): Promise<void>{
    this.menu = await this.dataservice.getMenu();
    if(this.menu == null){
      throw new Error("Menu cannot be null");
    }
    this.pizzas = this.menu.pizzaRows.map(t=> t.pizza);
    this.ingredients = this.menu.ingrRows.map(t => t.ingredient);
  }

  setMode(mode: string){
    this.mode = mode;
  }
}
