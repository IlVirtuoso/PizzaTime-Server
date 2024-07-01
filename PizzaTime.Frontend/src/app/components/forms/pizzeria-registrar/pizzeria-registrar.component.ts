import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { Router } from '@angular/router';
import { IDataBridge } from 'app/services/idatabridge';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';

@Component({
  selector: 'app-pizzeria-registrar',
  standalone: true,
  imports: [
    CardModule,
    FormsModule,
    PasswordModule,
    InputTextModule,
    FloatLabelModule,
    ButtonModule,
    
  ],
  templateUrl: './pizzeria-registrar.component.html',
  styleUrl: './pizzeria-registrar.component.css',
})
export class PizzeriaRegistrarComponent {
    protected name = '';
  
    protected vatNumber = '';
  
    protected address = '';
  
    protected errorMessage = '';
  
    public constructor(private router: Router, private bridge: IDataBridge) {}
  
    public async createPizzeria() {
      console.log('Create a pizzeria with name' + this.name);
      if (this.name == '' || this.vatNumber == '' || this.address == '') {
        this.errorMessage = 'Fields cannot be blank';
        return;
      }
      let t = await this.bridge.getUser();
      if (t != null && t.isVendor) {
        this.errorMessage = 'User already owns already';
        return;
      }
      let result = await this.bridge.createPizzeria(
        this.name,
        this.address,
        this.vatNumber
      );
      if (result == false) {
        this.router.navigateByUrl('/home');
        this.bridge.regModeOnly = true;
      } else this.router.navigateByUrl('login');
    }
  }
  