import {Component} from '@angular/core';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import { IDataBridge } from 'app/services/idatabridge';
import {MatCard, MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgTemplateOutlet } from '@angular/common';


@Component({
  selector: 'app-login-form',
  styleUrl: './login-form.component.css',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatSelectModule,MatCardModule,MatButtonModule, FormsModule,NgTemplateOutlet],
  templateUrl: './login-form.component.html',
})
export class LoginFormComponent {

  protected errorMessage = "";

  protected formFields = ["",""]

  public constructor( private service : IDataBridge, private router: Router){

  }

  public async login(){
    let result = await this.service.login(this.formFields[0], this.formFields[1]);
    if(result){
      console.log("Login success");
      this.router.navigateByUrl("/home");
    }
    else{
      this.errorMessage = "invalid credentials";
    }
  }

  public logout(){

  }
}
