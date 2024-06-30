import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { DataBridgeService } from './services/data-bridge/data-bridge.service';
import { IDataBridge } from './services/idatabridge';
import { CookieService } from 'ngx-cookie-service';
import axios, { Axios , AxiosRequestConfig} from 'axios';
import { HttpClient, HttpHandler, provideHttpClient } from '@angular/common/http';



export class Configuration{
  static demoMode : boolean = true;
}

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideAnimationsAsync(),
    {provide:IDataBridge, useClass: DataBridgeService}, provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(),{provide:Axios,useFactory:()=> axios},provideHttpClient()
  ],


};
