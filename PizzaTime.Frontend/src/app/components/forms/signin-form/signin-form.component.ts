import { NgTemplateOutlet } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormField, MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { IDataBridge } from 'app/services/idatabridge';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';

@Component({
  selector: 'app-signin-form',
  standalone: true,
  imports: [
    CardModule,
    FormsModule,
    PasswordModule,
    InputTextModule,
    FloatLabelModule,
    ButtonModule,
  ],
  templateUrl: './signin-form.component.html',
  styleUrl: './signin-form.component.css',
})
export class SigninFormComponent {

  protected username = "";

  protected firstName = "";

  protected lastName = "";



  protected  password = "";

  protected password_confirmation  = "";


  protected errorMessage = '';

  public constructor(private router: Router, private bridge: IDataBridge) {}

  public async signin() {
    console.debug('trying to login with username ' + this.username);
    if (this.password == '' || this.password_confirmation == '') {
      this.errorMessage = 'Passwords cannot be blank';
      return;
    }
    if (this.password != this.password_confirmation) {
      this.errorMessage = "Password doesn't match";
      return;
    }
    let t = await this.bridge.getUser();
    if (t != null) {
      this.errorMessage = 'User already exists';
      return;
    }
    let result = await this.bridge.signin(this.firstName,this.lastName,this.username,this.password);
    if (!result) {
      this.errorMessage = 'Something went wrong';
      return;
    }
    this.router.navigateByUrl('login');
  }
}
