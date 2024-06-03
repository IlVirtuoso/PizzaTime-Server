import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { IDataBridge } from 'app/services/idatabridge';
import { ButtonModule } from 'primeng/button';
import { OrderListModule } from 'primeng/orderlist';
import { PickListModule } from 'primeng/picklist';
import { CardModule } from 'primeng/card';
import { Ingredient, Pizza } from '@data/data-module.module';


@Component({
  selector: 'app-pizza-composer',
  standalone: true,
  imports: [PickListModule,ButtonModule,OrderListModule,CardModule],
  templateUrl: './pizza-composer.component.html',
  styleUrl: './pizza-composer.component.css'
})
export class PizzaComposerComponent implements OnInit{
  protected sourceIngredients : Ingredient[] = [];
  protected selectedIngredients : Ingredient[] = [];

  protected userPizzas : Pizza[]=[];

  public constructor(private router: Router, private bridge : IDataBridge) {

  }

  async ngOnInit(): Promise<void>{
    this.sourceIngredients = await this.bridge.getAvailableIngredients() ?? [];
  }





}
