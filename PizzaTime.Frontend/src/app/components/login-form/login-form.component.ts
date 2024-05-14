import {Component} from '@angular/core';
import {MatSelectModule} from '@angular/material/select';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import { IDataBridge } from 'app/services/idatabridge';
import {MatCard, MatCardModule} from '@angular/material/card';
import {MatButtonModule} from '@angular/material/button';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';


@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatSelectModule,MatCardModule,MatButtonModule],
  templateUrl: './login-form.component.html',
})
export class LoginFormComponent {
  public constructor( private service : IDataBridge){

  }

  public login(username: string, password: string){

  }

  public logout(){

  }
}
