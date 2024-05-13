import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { DataBridgeService } from './app/services/data-bridge/data-bridge.service';


bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
