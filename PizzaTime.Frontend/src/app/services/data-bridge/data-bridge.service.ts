import { Injectable } from '@angular/core';
import { IDataBridge } from '../idatabridge';
import { CookieService } from 'ngx-cookie-service';
import { Axios } from 'axios';
import { HttpClient } from '@angular/common/http';

class LoginRequest{constructor(public username: string, public password: string){}}



@Injectable({
  providedIn: 'root'
})
export class DataBridgeService {

  constructor(private cookieService: CookieService, 
    private promiseClient: Axios,
    private fetcher : HttpClient 
  ) {
    
  }


}
