import { GoogleLoginProvider, GoogleSigninButtonModule, SocialAuthService, SocialUser } from '@abacritt/angularx-social-login';
import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';

@Component({
  selector: 'app-sign-in-with-google',
  standalone: true,
  imports: [CommonModule, ImportsModule, GoogleSigninButtonModule],
  templateUrl: './sign-in-with-google.component.html',
  styleUrl: './sign-in-with-google.component.css',
})
export class SignInWithGoogleComponent {
  user: SocialUser | undefined;
  loggedIn: boolean | undefined;

  @Output() onSocialAuthentication: EventEmitter<SocialUser> = new EventEmitter(false);

  constructor(private authService: SocialAuthService) {}

  ngOnInit(): void {
    this.authService.authState.subscribe((user) => {
      this.user = user;
      this.loggedIn = user != null;
      this.onSocialAuthentication.emit(user);
    });
  }

  signInWithGoogle(): void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
  }

  signOut(): void {
    this.authService.signOut();
  }
}
