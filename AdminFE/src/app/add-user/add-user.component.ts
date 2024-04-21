import { Component,ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
 import { RegisterDTO } from '../dtos/user/register.dto';
@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrl: './add-user.component.scss'
})
export class AddUserComponent{  
@ViewChild('registerForm') registerForm!: NgForm;
  phoneNumber: string;
  password: string;
  retypePassword: string;
  fullName: string;
  address:string;
  dateOfBirth:Date;

constructor(private router: Router, private userService: UserService){
  this.phoneNumber='';
  this.password='';
  this.retypePassword='';
  this.fullName='';
  this.address='';
  this.dateOfBirth= new Date();
  this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear()-18);
  //inject 
}
onPhoneNumberChange(){
    console.log(`Phone typed: ${this.phoneNumber}`);
}
register(){
  const message = `phoneNumber: ${this.phoneNumber}`+ 
                  ` password: ${this.password}`+ 
                  ` retypePassword: ${this.retypePassword}` + 
                  ` fullName: ${this.fullName  }` + 
                  ` address: ${this.address}`+
                  ` dateOfBirth: ${this.dateOfBirth}`;               
  // alert(message);
  const registerDTO:RegisterDTO ={
    "fullname": this.fullName,
    "phone_number": this.phoneNumber,
    "address": this.address,
    "password": this.password,
    "retype_password":this.retypePassword,
    "date_of_birth": this.dateOfBirth,
    "facebook_account_id":0,
    "google_account_id":0,
    "role_id":1

  }
  // this.userService.register(registerDTO).subscribe({
  //   next:(response:any) => {
  //     alert("Đăng ký thành công");
  //     this.router.navigate(['/home']);
  // },
  // error:(error:any)=>{
  //   if (error.error.message === 'Phone number already exists') {
  //     alert('Số điện thoại đã tồn tại');
  //   } else {
  //     console.log(error);
  //   }
  // }
  // });
  this.userService.register(registerDTO).subscribe({
    next: (response: any) => {
      alert("Add user successfully");
      this.router.navigate(['/users']);
    },
    error: (error: any) => {
      console.log('Error:', error);
      if (error.error && error.error.message === 'Phone number already exists') {
        alert('Số điện thoại đã tồn tại');
      } else {
        alert('Đăng ký thất bại');
      }
    }
  });

}
checkPasswordsMatch(){
  if (this.password !== this.retypePassword){
    this.registerForm.form.controls['retypePassword'].setErrors({'passwordMismatch':true});
  }
  else{
    this.registerForm.form.controls['retypePassword'].setErrors(null);
  }
}
checkAge(){
  if(this.dateOfBirth){
    const today = new Date();
    const birthDate = new Date(this.dateOfBirth);
    let age = today.getFullYear()-birthDate.getFullYear();
    const monthDiff = today.getMonth()-birthDate.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate()<birthDate.getDate())){
      age--;
    }
    if (age<18){
      this.registerForm.form.controls['dateOfBirth'].setErrors({'invalidAge':true});
    }
    else{
      this.registerForm.form.controls['dateOfBirth'].setErrors(null);
    }
  }
}
}
