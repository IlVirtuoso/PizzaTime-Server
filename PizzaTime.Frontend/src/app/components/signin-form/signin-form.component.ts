import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormField, MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { IDataBridge } from 'app/services/idatabridge';

@Component({
  selector: 'app-signin-form',
  standalone: true,
  imports: [MatLabel,MatFormFieldModule,MatCardModule,MatInputModule,MatButtonModule,FormsModule],
  templateUrl: './signin-form.component.html',
  styleUrl: './signin-form.component.css'
})
export class SigninFormComponent {
  protected username= "";
  protected password = "";
  protected password_confirmation = "";
  protected errorMessage = "";

  public constructor(private router: Router, private bridge: IDataBridge){

  }



  public async signin(){
    if(this.password != this.password_confirmation){
      this.errorMessage = "Password doesn't match";
      return;
    }
    let t = await this.bridge.getUser(this.username);
    if(t != null){
      this.errorMessage = "User already exists";
      return;
    }
    let result = await this.bridge.signin(this.username, this.password);
    if(!result){
      this.errorMessage = "Something went wrong";
      return;
    }
    this.router.navigateByUrl("login");
  }
}
