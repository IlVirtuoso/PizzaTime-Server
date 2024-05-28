import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { DataBridgeService } from './services/data-bridge/data-bridge.service';
import { IDataBridge } from './services/idatabridge';
import { MockBridgeService } from './services/mock-bridge/mock-bridge.service';



export class Configuration{
  static demoMode : boolean = true;
}

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideAnimationsAsync(),
    {provide:IDataBridge, useFactory: () => {
      if(Configuration.demoMode){
        return new MockBridgeService();
      }
      else return new DataBridgeService();
    }}, provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync(), provideAnimationsAsync()
  ],


};
