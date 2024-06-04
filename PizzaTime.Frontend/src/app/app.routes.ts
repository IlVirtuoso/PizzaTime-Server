import { Routes } from '@angular/router';
import { LoginFormComponent } from './components/forms/login-form/login-form.component';
import { SigninFormComponent } from './components/forms/signin-form/signin-form.component';
import { UserHomeComponent } from './components/pages/user-home/user-home.component';
import { PizzaComposerComponent } from './components/views/pizza-composer/pizza-composer.component';
import { PizzaPartyPageComponent as PizzaPartyComponent } from './components/pages/pizza-party-page/pizza-party-page.component';
import { PizzeriaAdminPageComponent } from './components/pages/pizzeria-admin-page/pizzeria-admin-page.component';

export const routes: Routes = [
  {path : 'login', component: LoginFormComponent},
  {path: 'signin', component: SigninFormComponent},
  {path: 'home', component:UserHomeComponent},
  {path:'composer', component:PizzaComposerComponent},
  {path:'party', component:PizzaPartyComponent},
  {path:'pizzeriaAdmin', component:PizzeriaAdminPageComponent}
];
