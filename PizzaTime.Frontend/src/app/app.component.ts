import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToolBarComponent } from './components/views/tool-bar/tool-bar.component';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { IDataBridge } from './services/idatabridge';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,ToolBarComponent,MatCardModule,MatButtonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'PizzaTimeFrontend';

}
