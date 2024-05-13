import { Component } from '@angular/core';
import { IDataBridge } from '../../services/idatabridge';
import {MatFormField, MatFormFieldControl, MatLabel} from '@angular/material/form-field'
import {MatSelect} from '@angular/material/select'
import { MatOption } from '@angular/material/select';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [MatFormField,MatLabel,MatSelect,MatOption],
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
