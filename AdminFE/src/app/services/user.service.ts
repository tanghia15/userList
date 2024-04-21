import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError,map } from 'rxjs/operators';
import { RegisterDTO } from '../dtos/user/register.dto';
import { LoginDTO } from '../dtos/user/login.dto';
import { environment } from '../environments/environment';
import { User } from '../user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private isLoggedIn: boolean = false;
  private apiRegister = `${environment.apiBaseUrl}/users/register`;
  private apiLogin = `${environment.apiBaseUrl}/users/login`;
  private apiBaseUrl = environment.apiBaseUrl;
  private apiConfig = {
    headers: this.createHeaders(),
  }

  constructor(private http: HttpClient ) { }
  getUsers(): Observable<User[]> {
    const apiUrl = `${this.apiBaseUrl}/users/listUser`;
    return this.http.get<User[]>(apiUrl);
  }
  deleteUser(id: number): Observable<any> {
    const apiUrl = `${this.apiBaseUrl}/users/${id}`;
    return this.http.delete(apiUrl);
  }
  getUserById(userId: number): Observable<any> {
    return this.http.get(`${this.apiBaseUrl}/users/listUser/${userId}`);
  }

  updateUser(userId: number, userData: any): Observable<any> {
    return this.http.put(`${this.apiBaseUrl}/users/${userId}`, userData);
  }

  private createHeaders(): HttpHeaders {
    return new HttpHeaders({'Content-Type':'application/json'});
  }
  
  register(registerDTO: RegisterDTO): Observable<any> {
    return this.http.post(this.apiRegister, registerDTO, this.apiConfig)
      .pipe(
        catchError(error => {
          alert('Đăng ký thất bại: ' + error.error.message);
          return throwError(error);
        })
      );
  }

  login(loginDTO: LoginDTO): Observable<any> {
    return this.http.post(this.apiLogin, loginDTO, this.apiConfig)
      .pipe(
        catchError(error => {
          alert('Đăng nhập thất bại: ' + error.error.message);
          return throwError(error);
          
        }),
        map(response => {
          return response; // Trả về response từ server
        })
        );
      
  }
}
