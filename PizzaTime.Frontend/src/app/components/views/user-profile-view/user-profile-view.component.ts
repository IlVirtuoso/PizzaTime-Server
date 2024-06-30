import { CommonModule, NgFor, NgIf, NgTemplateOutlet } from '@angular/common';
import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChange,
  SimpleChanges,
} from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { User } from '@data';
import { IDataBridge } from 'app/services/idatabridge';
import { CardModule } from 'primeng/card';
import { TabMenuModule } from 'primeng/tabmenu';
import { MenuItem } from 'primeng/api';
import { DataViewModule } from 'primeng/dataview';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { UserField } from './userfield';


@Component({
  selector: 'app-user-profile-view',
  standalone: true,
  imports: [
    CardModule,
    TabMenuModule,
    DataViewModule,
    CommonModule,
    FormsModule,
    NgFor,
    NgIf,
    NgTemplateOutlet,
    InputTextModule,
    FloatLabelModule,
    InputTextModule,
    ButtonModule,
  ],
  templateUrl: './user-profile-view.component.html',
  styleUrl: './user-profile-view.component.css',
})
export class UserProfileViewComponent {
  @Input() user: User | undefined = undefined;

  public constructor() {}

  protected fields: UserField[] = [];

  setEditableField(field: UserField) {
    field.editable = !field.editable;
  }

  saveEditedField(field: UserField) {
    field.editable = false;
    field.value = 'Saved';
  }
  ngOnChanges(changes: any): void {
    this.fields = [
      {fieldName: 'UserName', value: this.user?.username, editable:true},
      { fieldName: 'First Name', value: this.user?.firstName, editable: false },
      { fieldName: 'Last Name', value: this.user?.surname, editable: false },
      { fieldName: 'Email', value: this.user?.email, editable: false },
      { fieldName: 'Phone', value: this.user?.phone, editable: false },
      { fieldName: 'Address', value: this.user?.address, editable: false },
    ];
  }

  ngOnInit(): void {
    this.ngOnChanges(undefined);
  }
}
