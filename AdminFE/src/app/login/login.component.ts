import { Component } from '@angular/core';
import { LoginDTO } from '../dtos/user/login.dto';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  phoneNumber: string = '';
  password: string = '';
  isLoggedIn: boolean = false; // Khai báo biến isLoggedIn

  constructor(private router: Router, private userService: UserService) { }

  onPhoneNumberChange() {
    console.log(`Phone typed: ${this.phoneNumber}`);
  }

  login() {
    const message = `phoneNumber: ${this.phoneNumber}` +
      ` password: ${this.password}`;

    // alert(message);
    const loginDTO: LoginDTO = {
      "phone_number": this.phoneNumber,
      "password": this.password
    }
    this.userService.login(loginDTO).subscribe({
      next: (response: any) => {
        this.isLoggedIn = true; // Đánh dấu người dùng đã đăng nhập thành công
        this.router.navigate(['/home']);
        alert("đăng nhập thành công");
        // this.userService.setPhoneNumber(response.phoneNumber);

      },

      error: (error: any) => {
        alert('Đăng nhập thất bại: ' + error.error.message);
      }
    });
  }
}
