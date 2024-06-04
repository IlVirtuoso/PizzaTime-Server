import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { User } from './User';
import { Ingredient } from './Ingredient';

export * from './Menu';
export * from './Ingredient';
export * from './message';
export * from './Order';
export * from './Pizza';
export * from './PizzaParty';
export * from './Pizzeria';
export * from './User';
export * from './Role';

@NgModule({
  declarations: [
    
  ],
  imports: [
    CommonModule
  ],
  
})
export class DataModuleModule { }
