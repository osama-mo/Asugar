import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AnimationOptions } from 'ngx-lottie';
import { ToastrService } from 'ngx-toastr';
import { throwError } from 'rxjs';
import { AuthService } from '../shared/auth.service';
import { LoginRequestPayload } from './login-request.payload';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    // assign error messages to be hidden 
    efvisibility = 'hidden'
    gvisibility = 'hidden'
  
  


  loginForm = new FormGroup({
    username: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required),
  })
  loginRequestPayload: LoginRequestPayload;
  registerSuccessMessage!: string;
  isError!: boolean;
  signedin: boolean = false;
  blablabla = this.authService.testAuth();

  
  constructor(private authService: AuthService, private activatedRoute: ActivatedRoute,
    private router: Router, private toastr: ToastrService) {
    this.loginRequestPayload = {
      username: '',
      password: ''
    };
  }

  ngOnInit(): void {
  }

  login() {


    this.efvisibility = 'hidden'
    this.gvisibility = 'hidden'


    if (this.loginForm.get('email')!.value?.length == 0 
    || this.loginForm.get('username')!.value?.length ==  0) {
      this.efvisibility = 'visible'
    }
    else {
      this.loginRequestPayload.username = this.loginForm.get('username')!.value;
      this.loginRequestPayload.password = this.loginForm.get('password')!.value;
  
      this.authService.login(this.loginRequestPayload).subscribe(data => {
        this.signedin = true;
        this.isError = false;
        this.toastr.success('Login Successful');
      }, error => {
        this.gvisibility = 'visible'
        this.isError = true;
        throwError(error);
      });
    }
  }
}
