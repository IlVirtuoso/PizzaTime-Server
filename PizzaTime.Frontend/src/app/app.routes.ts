import { Routes } from '@angular/router';
import { LoginFormComponent } from './components/forms/login-form/login-form.component';
import { SigninFormComponent } from './components/forms/signin-form/signin-form.component';
import { UserHomeComponent } from './components/pages/user-home/user-home.component';
import { PizzaComposerComponent } from './components/views/pizza-composer/pizza-composer.component';
import { PizzaPartyPageComponent as PizzaPartyComponent } from './components/pages/pizza-party-page/pizza-party-page.component';
import { PizzeriaAdminPageComponent } from './components/pages/pizzeria-admin-page/pizzeria-admin-page.component';
import { PizzaListViewComponent } from './components/views/pizza-list-view/pizza-list-view.component';
import { IngredientListViewComponent } from './components/views/ingredient-list-view/ingredient-list-view.component';
import { PizzaMenuAdderComponent } from './components/views/pizza-menu-adder/pizza-menu-adder.component';
import { IngredientsMenuAdderComponent } from './components/views/ingredients-menu-adder/ingredients-menu-adder.component';

export const routes: Routes = [
  {path : 'login', component: LoginFormComponent},
  {path: 'signin', component: SigninFormComponent},
  {path: 'home', component:UserHomeComponent},
  {path:'composer', component:PizzaComposerComponent},
  {path:'party', component:PizzaPartyComponent},
  {path:'pizzeriamanager', component:PizzeriaAdminPageComponent},
  {path:'mpc', component:IngredientsMenuAdderComponent}
];
