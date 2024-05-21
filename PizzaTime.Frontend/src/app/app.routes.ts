import { Routes } from '@angular/router';
import { LoginFormComponent } from './components/login-form/login-form.component';
import { SigninFormComponent } from './components/signin-form/signin-form.component';
import { UserHomeComponent } from './components/user-home/user-home.component';

export const routes: Routes = [
  {path : 'login', component: LoginFormComponent},
  {path: 'signin', component: SigninFormComponent},
  {path: 'home', component:UserHomeComponent}
];
