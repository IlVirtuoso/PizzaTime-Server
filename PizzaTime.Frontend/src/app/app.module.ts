import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { BrowserModule } from '@angular/platform-browser';
import { CookieService } from 'ngx-cookie-service';



@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    MatFormFieldModule,
    BrowserModule
  ],
  providers:[CookieService]
})
export class AppModule { }
