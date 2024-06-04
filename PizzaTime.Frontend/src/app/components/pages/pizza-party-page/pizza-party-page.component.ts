import { Component } from '@angular/core';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';

@Component({
  selector: 'app-pizza-party-page',
  standalone: true,
  imports: [ImportsModule],
  templateUrl: './pizza-party-page.component.html',
  styleUrl: './pizza-party-page.component.css'
})
export class PizzaPartyPageComponent {

}
