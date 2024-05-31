import { Routes } from '@angular/router';
import { LoginFormComponent } from './components/forms/login-form/login-form.component';
import { SigninFormComponent } from './components/forms/signin-form/signin-form.component';
import { UserHomeComponent } from './components/pages/user-home/user-home.component';
import { PizzaComposerComponent } from './components/views/pizza-composer/pizza-composer.component';

export const routes: Routes = [
  {path : 'login', component: LoginFormComponent},
  {path: 'signin', component: SigninFormComponent},
  {path: 'home', component:UserHomeComponent},
  {path:'composer', component:PizzaComposerComponent}
];
