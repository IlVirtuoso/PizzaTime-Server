import { Component, OnInit } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { User } from '@data/User';
import { IDataBridge } from 'app/services/idatabridge';

@Component({
  selector: 'app-user-home',
  standalone: true,
  imports: [MatCardModule,MatIconModule],
  templateUrl: './user-home.component.html',
  styleUrl: './user-home.component.css'
})
export class UserHomeComponent implements OnInit {

  protected user : User| null = null;

  public constructor(private router : Router, private bridge: IDataBridge){

  }

  ngOnInit(): void {
    //this.user = this.bridge.getAuthenticatedUser();

    this.user = new User(
      "test",
      "test@test.com",
      "test",
      "test"
    )
  }





}
