import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { DataBridgeService } from './services/data-bridge/data-bridge.service';
import { IDataBridge } from './services/idatabridge';
import { CookieService } from 'ngx-cookie-service';
import axios, { Axios , AxiosRequestConfig} from 'axios';
import { HttpClient, HttpHandler, provideHttpClient } from '@angular/common/http';
import { GoogleLoginProvider, SocialAuthServiceConfig} from '@abacritt/angularx-social-login';



export class Configuration{
  static demoMode : boolean = true;
}

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideAnimationsAsync(),
    {provide:IDataBridge, useClass: DataBridgeService}, provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(),{provide:Axios,useFactory:()=> axios},provideHttpClient(),

  {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false, // Mette a false se vuoi che l'utente debba fare login manualmente ogni volta
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
              '487413251074-makke9n1f0ot1kn7me3ho4hcbt4h929b.apps.googleusercontent.com'
            )
          }
        ]
      } as SocialAuthServiceConfig
    },

  ],


};
