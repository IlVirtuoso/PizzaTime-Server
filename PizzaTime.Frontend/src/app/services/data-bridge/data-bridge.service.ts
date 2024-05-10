import { Injectable } from '@angular/core';
import { ConfigProviderService } from '../config-provider/config-provider.service';


@Injectable({
  providedIn: 'root'
})
export class DataBridgeService{

  constructor(private configuration: ConfigProviderService) {

  }


}
