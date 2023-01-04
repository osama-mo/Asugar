import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SignupComponent } from './components/auth/signup/signup.component';
import { LoginComponent } from './components/auth/login/login.component';
import { ForgotMyPasswordComponent } from './components/auth/forgot-my-password/forgot-my-password.component';
import { ForgotMyPasswordConfirmationComponent } from './components/auth/forgot-my-password-confirmation/forgot-my-password-confirmation.component';
import { ListProjectComponent } from './components/project/list-project/list-project.component';
import { CreateProjectComponent } from './components/project/create-project/create-project.component';
import { BacklogComponent } from './components/backlog/backlog.component';
import { MembersListComponent } from './components/members/members-list/members-list.component';
import { MemberDetailsComponent } from './components/members/member-details/member-details.component';
import { AddMemberComponent } from './components/members/add-member/add-member.component';
import { ActiveSprintComponent } from './components/sprints/active-sprint/active-sprint.component';
import { MainIssuesComponent } from './components/issues/main-issues/main-issues.component';
import { CreateIssueComponent } from './components/issues/create-issue/create-issue.component';
import { RemoveIssueComponent } from './components/issues/remove-issue/remove-issue.component';
import { CreateEpicComponent } from './components/epics/create-epic/create-epic.component';
import { DeleteEpicComponent } from './components/epics/delete-epic/delete-epic.component';
import { EpicListComponent } from './components/epics/epic-list/epic-list.component';
import { CreateSubIssueComponent } from './components/epics/create-sub-issue/create-sub-issue.component';
import { IssuesListComponent } from './components/epics/issues-list/issues-list.component';
import { DeleteSubIssueComponent } from './components/epics/delete-sub-issue/delete-sub-issue.component';
import { AssignToEpicComponent } from './components/issues/assign-to-epic/assign-to-epic.component';




const routes: Routes = [

  {path:'',component:LoginComponent},
  {path:'signup',component:SignupComponent},
  {path:'forgot-my-password',component:ForgotMyPasswordComponent},
  {path:'forgot-my-password-confirmation',component:ForgotMyPasswordConfirmationComponent},
  {path:'list-project',component:ListProjectComponent},
  {path:'create-project',component:CreateProjectComponent},
  {path:'backlog',component:BacklogComponent},
  {path:'memberslist',component:MembersListComponent},
  {path:'member-details',component:MemberDetailsComponent},
  {path:'add-member',component:AddMemberComponent},
  {path:'active-sprint',component:ActiveSprintComponent},
  {path:'create-issue',component:CreateIssueComponent},
  {path:'remove-issue',component:RemoveIssueComponent},
  {path:'issues',component:MainIssuesComponent},
  {path:'epics-list',component:EpicListComponent},
  {path:'delete-epic',component:DeleteEpicComponent},
  {path:'create-epic',component:CreateEpicComponent},
  {path:'create-sub-issue',component:CreateSubIssueComponent},
  {path:'sub-issues-list',component:IssuesListComponent},
  {path:'delete-sub-issue',component:DeleteSubIssueComponent},
  {path:'assign-to-epic',component:AssignToEpicComponent},
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

