import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { IDataBridge } from 'app/services/idatabridge';

@Component({
  selector: 'app-signin-form',
  standalone: true,
  imports: [],
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


  public signin(){

  }
}
