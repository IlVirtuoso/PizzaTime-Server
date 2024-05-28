import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { IDataBridge } from 'app/services/idatabridge';

@Component({
  selector: 'app-pizza-composer',
  standalone: true,
  imports: [],
  templateUrl: './pizza-composer.component.html',
  styleUrl: './pizza-composer.component.css'
})
export class PizzaComposerComponent {

  public constructor(private router: Router, private bridge : IDataBridge) {

  }



}
