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
import { CardModule } from 'primeng/card';
import { PasswordModule } from 'primeng/password';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { ButtonModule } from 'primeng/button';
import { SignInWithGoogleComponent } from '../sign-in-with-google/sign-in-with-google.component';
import { SocialUser } from '@abacritt/angularx-social-login';
import { CookieService } from 'ngx-cookie-service';


@Component({
  selector: 'app-login-form',
  styleUrl: './login-form.component.css',
  standalone: true,
  imports: [CardModule,FormsModule,PasswordModule,InputTextModule,FloatLabelModule,ButtonModule
    ,SignInWithGoogleComponent
  ],
  templateUrl: './login-form.component.html',
})
export class LoginFormComponent {

  protected errorMessage = "";

  protected password= "";
  protected email = "";

  public constructor( private service : IDataBridge, private router: Router, private cookieService: CookieService){

  }

  public async login(){
    let result = await this.service.login(this.email, this.password);
    if(result == 0){
      console.log("Login success");
      this.router.navigateByUrl("/home");
    }
    else if(result == 206){
      this.router.navigateByUrl("/home");
    }
    else{
      this.errorMessage = this.service.lastError;
    }
  }

  public async socialLogin(user: SocialUser){
    this.cookieService.set("Authorization", user.idToken);
    var result = await this.service.socialLogin();
    if(result == 206){
      this.router.navigateByUrl("/home");
    }
    else{
      this.errorMessage = this.service.lastError;
    }
  }
  public logout(){

  }
}
