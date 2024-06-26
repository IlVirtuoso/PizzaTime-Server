import { Component } from '@angular/core';

@Component({
  selector: 'app-sign-in-with-google',
  standalone: true,
  imports: [],
  templateUrl: './sign-in-with-google.component.html',
  styleUrl: './sign-in-with-google.component.css'
})
export class SignInWithGoogleComponent {

  constructor(){

  }


  onSocialLogin(response: any){
    console.log(response);
  }

}
