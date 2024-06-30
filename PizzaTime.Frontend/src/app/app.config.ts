import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { DataBridgeService } from './services/data-bridge/data-bridge.service';
import { IDataBridge } from './services/idatabridge';
import { CookieService } from 'ngx-cookie-service';
import { Axios } from 'axios';
import { HttpClient, HttpHandler, provideHttpClient } from '@angular/common/http';



export class Configuration{
  static demoMode : boolean = true;
}

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideAnimationsAsync(),
    {provide:IDataBridge, useClass: DataBridgeService}, provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(),{provide:Axios,useFactory:()=> new Axios()},provideHttpClient()
  ],


};
