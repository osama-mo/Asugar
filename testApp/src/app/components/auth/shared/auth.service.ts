import { Injectable, Output, EventEmitter } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { SignupRequestPayload } from '../signup/signup-request.payload';
import { Observable, throwError } from 'rxjs';
import { LoginRequestPayload } from '../login/login-request.payload';
import { LoginResponse } from '../login/login-response.payload';
import { map, tap } from 'rxjs/operators';
import { ForgotMyPasswordRequestPayload } from '../forgot-my-password/forgot-my-password-request-payload';
import { ForgotMyPasswordConfirmationRequestPayload } from '../forgot-my-password-confirmation/forgot-my-password-confirmation-request.payload';
import { ProjectRequsetPayload } from 'app/components/project/list-project/project-request-payload';
import { Data } from '@angular/router';
import { IssueResponsePayload } from 'app/components/sprints/active-sprint/issue-respone-payload';
import { CreateIssueRequestPayload } from 'app/components/issues/create-issue/create-issue-request-payload';
import { CreateEpicRequestPayload } from 'app/components/epics/create-epic/create-epic-reques-payload';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  usernameo: string = "";

  @Output() loggedIn: EventEmitter<boolean> = new EventEmitter();
  @Output() username: EventEmitter<string> = new EventEmitter();

  refreshTokenPayload = {
    refreshToken: this.getRefreshToken(),
    username: this.getUserName()
  }

  url='https://asugarappservice-asugarapi.azuremicroservices.io/'

  constructor(private httpClient: HttpClient,

  ) {
  }

  signup(signupRequestPayload: SignupRequestPayload): Observable<any> {
    return this.httpClient.post(this.url+'registration', signupRequestPayload, { responseType: 'text' });
  }

  login(loginRequestPayload: LoginRequestPayload): Observable<boolean> {
    const httpOptions = {
      headers: new HttpHeaders({
        'username': loginRequestPayload.username!,
        'password': loginRequestPayload.password!
      })
    };

    return this.httpClient.post<LoginResponse>(this.url+'login',
      null, httpOptions).pipe(map(data => {
        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('username', loginRequestPayload.username!);
        localStorage.setItem('refreshToken', data.refreshToken);
        this.loggedIn.emit(true);
        this.username.emit(loginRequestPayload.username!);
        return true;
      }));
  }

  forgetMyPassword(fgmRequestPayload: ForgotMyPasswordRequestPayload): Observable<any> {
    return this.httpClient.get(this.url+`password_reset?email=${fgmRequestPayload.email}`)
  }

  forgetMyPasswordConfirmation(fgmcRequestPayload: ForgotMyPasswordConfirmationRequestPayload): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Token': fgmcRequestPayload.token!,
        'Password': fgmcRequestPayload.password!
      })
    };
    return this.httpClient.post(this.url+`password_reset`, null, httpOptions)
  }



  getJwtToken() {
    return localStorage.getItem('accessToken');
  }

  refreshToken() {
    return this.httpClient.post<LoginResponse>(this.url+'token/refresh',
      this.refreshTokenPayload)
      .pipe(tap(response => {
        localStorage.removeItem('accessToken');
        localStorage.setItem('accessToken',
          response.accessToken);
      }));
  }

  testAuth() {
    return this.httpClient.get<string>(this.url+'user/get');
  }



  logout() {
    this.loggedIn.emit(false);
    localStorage.removeItem('accessToken');
    localStorage.removeItem('username');
    localStorage.removeItem('accessToken');
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

  createProject(projectName: String | null) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.post(this.url+`user/project/create?name=${projectName}&username=${localStorage.getItem('username')}`, null, httpOptions)
  }

  getProjectlist(): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.get(this.url+`user/project/list`, httpOptions)
  }

  deleteProject(projectId: number) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.delete(this.url+`user/project/${projectId}`, httpOptions)
  }

  getMembersList(projectId: Number): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.get(this.url+`${projectId}/members`, httpOptions)
  }

  getMembersDetails(projectId: String, userEmail: String): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.get(this.url+`${projectId}/members/${userEmail}`, httpOptions)
  }

  addMember(projectId: String, userEmail: String): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.put(this.url+`${projectId}/members?username=${userEmail}`,null, httpOptions)
  }

  createIssue(issue: CreateIssueRequestPayload ,projectId : String): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.post(this.url+`${projectId}/issues/create`, issue, httpOptions);
  }

  removeIssue(issueId: string,projectId:string): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.delete(this.url+`${projectId}/issues/${issueId}/delete`, httpOptions)
  }


  getIssues(projectId:string): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.get(this.url+`${projectId}/issues/all`, httpOptions)
  }

  finishSprint(projectId:string): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.put(this.url+`${projectId}/sprints/finish`,null, httpOptions)
  }

  setCondition(projectId:number,issueId:number,condition:string): Observable<any>{
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.put(this.url+`${projectId}/issues/${issueId}/condition?condition=${condition}`,null, httpOptions)
  }

  createEpic(projectId:number,createEpicRequset:CreateEpicRequestPayload) : Observable<any>{
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.post(this.url+`${projectId}/epics/create`, createEpicRequset, httpOptions);
  }
  deleteEpic(projectId:number,epicId:number) : Observable<any>{
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.delete(this.url+`${projectId}/epics/${epicId}/delete`, httpOptions)
  }
  getEpicIssues(projectId:number,epicId:number) : Observable<any>{
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.get(this.url+`${projectId}/epics/${epicId}/issues`, httpOptions)
  }

  assignIssueToEpic(projectId:number,issueId:number,epicId:number|null) : Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.put(this.url+`${projectId}/issues/${issueId}/assign/epic?epicId=${epicId}`, null, httpOptions)
  }
  getEpics(projectId:number) : Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.get(this.url+`${projectId}/epics`, httpOptions)
  }

  finishEpic(epicId:number,projectId:number): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.put(this.url+`${projectId}/epics/${epicId}/finish`,null, httpOptions)
  }
  getActiveIssues(projectId:number) : Observable<any>{
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.get(this.url+`${projectId}/issues/active`, httpOptions)
  }

  setSprint(projectId:number,issueId:number,sprint:string|null): Observable<any> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.put(this.url+`${projectId}/issues/${issueId}/assign/sprint?sprint=${sprint}`,null, httpOptions)
  }

  removeMember(projectId:number,userEmail:string) : Observable<any>{
    const httpOptions = {
      headers: new HttpHeaders({
        'Authorization': localStorage.getItem('accessToken')!
      })
    }
    return this.httpClient.delete(this.url+`${projectId}/members?username=${userEmail}`, httpOptions)
  }
}
