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
  ],
  imports: [
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
