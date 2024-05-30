import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Router } from '@angular/router';
import { Pizzeria } from '@data/Pizzeria';
import { IDataBridge } from 'app/services/idatabridge';

@Component({
  selector: 'app-pizzeria-registrar',
  standalone: true,
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatCardModule,
    MatButtonModule,
    FormsModule,
  ],
  templateUrl: './pizzeria-registrar.component.html',
  styleUrl: './pizzeria-registrar.component.css',
})
export class PizzeriaRegistrarComponent {
  protected formFields = ['', '', '', '', '', ''];
  protected get piva() {
    return this.formFields[0];
  }
  protected get name() {
    return this.formFields[1];
  }
  protected get email() {
    return this.formFields[2];
  }
  protected get phone(){
    return this.formFields[3];
  }
  protected get address() {
    return this.formFields[4];
  }

  protected errorMessage = '';

  public constructor(private router: Router, private bridge: IDataBridge) {}

  register(): void {
    let pizzeria = new Pizzeria(this.email,this.phone,this.piva,this.name,this.address);
    if(!this.bridge.registerPizzeria(pizzeria)){
      this.errorMessage = this.bridge.lastError;
    }
    else{
      this.router.navigateByUrl('pizzeriaAdmin');
    }
  }
}
