import { Injectable, Output, EventEmitter } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SignupRequestPayload } from '../signup/signup-request.payload';
import { Observable, throwError } from 'rxjs';
import { LoginRequestPayload } from '../login/login-request.payload';
import { LoginResponse } from '../login/login-response.payload';
import { map, tap } from 'rxjs/operators';

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
    return this.httpClient.post<LoginResponse>('http://localhost:8080/api/auth/login',
      loginRequestPayload).pipe(map(data => {
        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('username', loginRequestPayload.username!);
        localStorage.setItem('refreshToken', data.refreshToken);

        this.loggedIn.emit(true);
        this.username.emit(loginRequestPayload.username!);
        return true;
      }));
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

  // logout() {
  //   this.httpClient.post('http://localhost:8080/api/auth/logout', this.refreshTokenPayload,
  //     { responseType: 'text' })
  //     .subscribe(data => {
  //       console.log(data);
  //     }, error => {
  //       throwError(error);
  //     })
  //   this.localStorage.clear('authenticationToken');
  //   this.localStorage.clear('username');
  //   this.localStorage.clear('refreshToken');
  //   this.localStorage.clear('expiresAt');
  // }

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
