import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../user.model';
@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss'
})
export class UserListComponent implements OnInit {
  users: User[] = [];
  deleteSuccessMessage: string = '';
  deleteErrorMessage: string = '';
  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.userService.getUsers().subscribe(
      (users: User[]) => {
        this.users = users;
      },
      (error) => {
        console.error('Error fetching users:', error);
      }
    );
  }

 
  deleteUser(id: number) {
    this.userService.deleteUser(id).subscribe(
      () => {
        // Xóa người dùng khỏi danh sách khi xóa thành công
        const isDelete = confirm("Do you want to delete ?")
        if (isDelete){
          this.users = this.users.filter(user => user.id !== id);
          alert('User deleted successfully');
        }
        
      },
      (error) => {
        console.error('Error deleting user:', error);
        // Hiển thị thông báo lỗi
        alert('Failed to delete user');
      }
    );
  }

}
