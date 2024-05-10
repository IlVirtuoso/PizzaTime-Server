import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigProviderService {
  public static mode = "Production";
  public get mode() : string{
    return this.mode;
  }
  constructor() { }
}


