import { Component, OnInit ,ViewChild} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { NgForm } from '@angular/forms';
import { formatDate } from '@angular/common';
@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrl: './edit-user.component.scss'
})
export class EditUserComponent implements OnInit {
  @ViewChild('registerForm') registerForm!: NgForm;
  userId: number=0;
  // dateOfBirth: string = '';
  user: any = {}; // Đối tượng user để hiển thị thông tin và thực hiện cập nhật

  constructor(private route: ActivatedRoute, private userService: UserService,private router: Router){ }

  ngOnInit(): void {
    this.userId = this.route.snapshot.params['id'];


    // Gọi phương thức getUserById() từ UserService để lấy thông tin người dùng
    this.userService.getUserById(this.userId).subscribe(
      (data: any) => {
        this.user = data; // Lưu thông tin người dùng vào đối tượng user
         // Chuyển đổi định dạng ngày tháng năm của user.date_of_birth sang Date
         this.user.date_of_birth = formatDate(this.user.date_of_birth, 'yyyy-MM-dd', 'en-US');
        // Check age after getting user data
        //this.checkAge();
      },
      (error: any) => {
        console.error('Error fetching user data:', error);
        // Xử lý lỗi, ví dụ: hiển thị thông báo lỗi cho người dùng
      }
    );
  }

  updateUser(): void {
    this.userService.updateUser(this.userId, this.user).subscribe(
      (data: any) => {
        console.log('User updated successfully:', data);
        this.router.navigate(['/users']);
      },
      (error: any) => {
        console.error('Error updating user:', error);
      }
    );
  }

// checkAge(){
//   if(this.dateOfBirth){
//     const today = new Date();
//     const birthDate = new Date(this.dateOfBirth);
//     let age = today.getFullYear()-birthDate.getFullYear();
//     const monthDiff = today.getMonth()-birthDate.getMonth();
//     if (monthDiff < 0 || (monthDiff === 0 && today.getDate()<birthDate.getDate())){
//       age--;
//     }
//     if (age<18){
//       this.registerForm.form.controls['dateOfBirth'].setErrors({'invalidAge':true});
//     }
//     else{
//       this.registerForm.form.controls['dateOfBirth'].setErrors(null);
//     }
//   }
// }

}