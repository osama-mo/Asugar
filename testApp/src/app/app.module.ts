import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/auth/login/login.component';
import { SignupComponent } from './components/auth/signup/signup.component';
import { HeaderComponent } from './components/header/header.component';
import { ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { LottieModule } from 'ngx-lottie';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ToastrModule } from 'ngx-toastr';
import { ForgotMyPasswordComponent } from './components/auth/forgot-my-password/forgot-my-password.component';
import { ForgotMyPasswordConfirmationComponent } from './components/auth/forgot-my-password-confirmation/forgot-my-password-confirmation.component';
import { ListProjectComponent } from './components/project/list-project/list-project.component';
import { CreateProjectComponent } from './components/project/create-project/create-project.component';

import { BacklogComponent } from './components/backlog/backlog.component';
import { MembersListComponent } from './components/members/members-list/members-list.component';
import { MemberDetailsComponent } from './components/members/member-details/member-details.component';
import { AddMemberComponent } from './components/members/add-member/add-member.component';

import { DragDropModule } from '@angular/cdk/drag-drop';
import { ActiveSprintComponent } from './components/sprints/active-sprint/active-sprint.component';
import { MainIssuesComponent } from './components/issues/main-issues/main-issues.component';
import { CreateIssueComponent } from './components/issues/create-issue/create-issue.component';
import { RemoveIssueComponent } from './components/issues/remove-issue/remove-issue.component';
import { EpicListComponent } from './components/epics/epic-list/epic-list.component';
import { CreateEpicComponent } from './components/epics/create-epic/create-epic.component';
import { DeleteEpicComponent } from './components/epics/delete-epic/delete-epic.component';
import { IssuesListComponent } from './components/epics/issues-list/issues-list.component';
import { CreateSubIssueComponent } from './components/epics/create-sub-issue/create-sub-issue.component';
import { DeleteSubIssueComponent } from './components/epics/delete-sub-issue/delete-sub-issue.component';
import { AssignToEpicComponent } from './components/issues/assign-to-epic/assign-to-epic.component';




// import { TokenInterceptor } from './token-interceptor';

export function playerFactory(): any {  
  return import('lottie-web');
}
@NgModule({
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    HeaderComponent,
    ForgotMyPasswordComponent,
    ForgotMyPasswordConfirmationComponent,
    ListProjectComponent,
    CreateProjectComponent,
    BacklogComponent,
    MembersListComponent,
    MemberDetailsComponent,
    AddMemberComponent,
    ActiveSprintComponent,
    MainIssuesComponent,
    CreateIssueComponent,
    RemoveIssueComponent,
    EpicListComponent,
    CreateEpicComponent,
    DeleteEpicComponent,
    IssuesListComponent,
    CreateSubIssueComponent,
    DeleteSubIssueComponent,
    AssignToEpicComponent,
  ],
  imports: [
    DragDropModule,
    HttpClientModule,
    BrowserModule,
    BrowserAnimationsModule,
    LottieModule.forRoot({ player: playerFactory }),
    ReactiveFormsModule,
    FontAwesomeModule,
    AppRoutingModule,
    ToastrModule.forRoot({
      positionClass :'toast-bottom-right'
    })
  ],
  providers: [
 
    // {
    // provide: HTTP_INTERCEPTORS,
    // useClass: TokenInterceptor,
    // multi: true
    // },
    
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
