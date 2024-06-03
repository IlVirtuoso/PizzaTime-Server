import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIcon, MatIconModule } from '@angular/material/icon';
import { MatButton, MatButtonModule } from '@angular/material/button';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';
import { MegaMenuItem, MenuItem } from 'primeng/api';
import { IDataBridge } from 'app/services/idatabridge';
import { Router } from '@angular/router';

@Component({
  selector: 'toolbar-component',
  standalone: true,
  imports: [ImportsModule],
  templateUrl: './tool-bar.component.html',
  styleUrl: './tool-bar.component.css'
})
export class ToolBarComponent {

  protected items: MenuItem[] = [
    { label: 'Login', url:'login', command: (item) => this.navigate(item.item, false) },
    { label: 'SignIn', url: 'signin', command: (item) => this.navigate(item.item, false) },
    { label: 'Home',url:'home' ,command: (item) => this.navigate(item.item, true) },
    { label: 'Pizza Party', url:'party', command: (item) => this.navigate(item.item, true) },
    { label: 'Contacts', url:'contacts',command: (item) => this.navigate(item.item, false) },
    { label: 'About us', url:'aboutus' ,command: (item) => this.navigate(item.item, false) },
  ];

  public constructor(protected service: IDataBridge, protected router: Router) { }

  public navigate(item: MenuItem | MegaMenuItem | undefined, requiresAuthorization: boolean) {
    console.log(item?.label);
    if (requiresAuthorization) {
      if (this.service.getAuthenticatedUser() == null) {
        this.router.navigateByUrl('login');
      }
    }
    if (item?.url != null)
      this.router.navigateByUrl(item?.url);
  }
}