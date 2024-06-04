import { Component, EventEmitter, Input, Output } from '@angular/core';
import { User } from '@data';
import { ImportsModule } from 'app/imports/prime-ng/prime-ng.module';

@Component({
  selector: 'app-user-list-view',
  standalone: true,
  imports: [ImportsModule],
  templateUrl: './user-list-view.component.html',
  styleUrl: './user-list-view.component.css'
})
export class UserListViewComponent {
  @Input() public users: User[] = [];
  @Output() public onUserSelect: EventEmitter<User> = new EventEmitter<User>();

  public userSelected(user:User){
    this.onUserSelect.emit(user);
  }
  
}
