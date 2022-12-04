import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SignupComponent } from './components/auth/signup/signup.component';
import { LoginComponent } from './components/auth/login/login.component';
import { ForgotMyPasswordComponent } from './components/auth/forgot-my-password/forgot-my-password.component';
import { ForgotMyPasswordConfirmationComponent } from './components/auth/forgot-my-password-confirmation/forgot-my-password-confirmation.component';
import { ResetPasswordComponent } from './components/auth/reset-password/reset-password.component';



const routes: Routes = [
  {path:'',component:LoginComponent},
  {path:'signup',component:SignupComponent},
  {path:'forgot-my-password',component:ForgotMyPasswordComponent},
  {path:'forgot-my-password-confirmation',component:ForgotMyPasswordConfirmationComponent},
  {path:'reset-password',component:ResetPasswordComponent}

];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

