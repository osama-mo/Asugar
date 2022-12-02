import { Injectable, Output, EventEmitter } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { SignupRequestPayload } from '../signup/signup-request.payload';
import { Observable, throwError } from 'rxjs';
import { LoginRequestPayload } from '../login/login-request.payload';
import { LoginResponse } from '../login/login-response.payload';
import { map, tap } from 'rxjs/operators';
import { ForgotMyPasswordRequestPayload } from '../forgot-my-password/forgot-my-password-request-payload';
import { ForgotMyPasswordConfirmationRequestPayload } from '../forgot-my-password-confirmation/forgot-my-password-confirmation-request.payload';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  @Output() loggedIn: EventEmitter<boolean> = new EventEmitter();
  @Output() username: EventEmitter<string> = new EventEmitter();

  refreshTokenPayload = {
    refreshToken: this.getRefreshToken(),
    username: this.getUserName()
  }

  constructor(private httpClient: HttpClient,

    ) {
  }

  signup(signupRequestPayload: SignupRequestPayload): Observable<any> {
    return this.httpClient.post('http://localhost:8080/registration', signupRequestPayload, { responseType: 'text' });
  }

  login(loginRequestPayload: LoginRequestPayload): Observable<boolean> {
    let body = new URLSearchParams();
    body.set('username', loginRequestPayload.username!);
    body.set('password',loginRequestPayload.password!);
    let options = {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    };
    return this.httpClient.post<LoginResponse>('http://localhost:8080/login',
      body.toString(),options).pipe(map(data => {
        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('username', loginRequestPayload.username!);
        localStorage.setItem('refreshToken', data.refreshToken);

        this.loggedIn.emit(true);
        this.username.emit(loginRequestPayload.username!);
        return true;
      }));
  }
  forgetMyPassword(fgmRequestPayload: ForgotMyPasswordRequestPayload) : Observable<any> {
    return this.httpClient.get(`localhost:8080/password_reset?email=ّ{${fgmRequestPayload.email}}` )
  }

  forgetMyPasswordConfirmation(fgmcRequestPayload: ForgotMyPasswordConfirmationRequestPayload) : Observable<any> {
    return this.httpClient.post(`localhost:8080/password_reset`, fgmcRequestPayload, { responseType: 'text' })
  }
  
  getJwtToken() {
    return localStorage.getItem('accessToken');
  }

  refreshToken() {
    return this.httpClient.post<LoginResponse>('http://localhost:8080/token/refresh',
      this.refreshTokenPayload)
      .pipe(tap(response => {
        localStorage.removeItem('accessToken');
        localStorage.setItem('accessToken',
          response.accessToken);
      }));
  }

  testAuth(){
    return this.httpClient.get<string>('http://localhost:8080/user/get');
  }



  logout() {
    this.httpClient.post('http://localhost:8080/api/auth/logout', this.refreshTokenPayload,
      { responseType: 'text' })
      .subscribe(data => {
        console.log(data);
      }, error => {
        throwError(error);
      })
    localStorage.clear();
  }

  getUserName() {
    return localStorage.getItem('username');
  }
  getRefreshToken() {
    return localStorage.getItem('refreshToken');
  }

  isLoggedIn(): boolean {
    return this.getJwtToken() != null;
  }
}
