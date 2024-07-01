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
  @Input() public user: User | null = null;

  public constructor(private dataservice: IDataBridge) {}

  protected fields: UserField[] = [];


  protected userName : string = this.user?.username ?? "";
  protected firstName : string = this.user?.firstName ?? "";
  protected lastName : string = this.user?.lastName?? "";
  protected address : string = this.user?.address??"";
  protected mobile : string = this.user?.mobile??"";


  setEditableField(field: UserField) {
    field.editable = !field.editable;
  }

  saveEditedField(field: UserField) {
    field.editable = false;
    field.value = 'Saved';
  }
  ngOnChanges(changes: any): void {
    this.fields = [
      {fieldName: 'UserName', value: this.userName, editable:false},
      { fieldName: 'First Name', value: this.firstName, editable: true },
      { fieldName: 'Last Name', value: this.lastName, editable: true },
      { fieldName: 'Email', value: this.user?.email, editable: false },
      { fieldName: 'Phone', value: this.user?.phone, editable: true },
      { fieldName: 'Address', value: this.user?.address, editable: true },
    ];
  }

  ngOnInit(): void {
    this.ngOnChanges(undefined);
  }

  async applyChanges(): Promise<void>{
    if(this.user == null){
      throw new Error("User cannot be null");
    }

    let t = await this.dataservice.getUser();
    if (t != null) {
      //this.errorMessage = 'User already owns already';
      var result = await this.dataservice.setUserData(
        this.firstName,
        this.lastName,
        this.address,
        this.mobile,
        '0000'
      );    
    }else{
      var resultReg = await this.dataservice.finalizeRegistration(
        'Matteo',
        'Ielacqua',
        'Viale vittoria 37',
        '0000',
        '0000'
      );
      if(resultReg == 0){
        this.dataservice.regModeOnly = false;
      }
    }
  

    

    

  }
}
