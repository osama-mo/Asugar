import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { isEmpty } from 'rxjs';
import { AuthService } from '../shared/auth.service';
import { SignupRequestPayload } from './signup-request.payload';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  // assign error messages to be hidden 
  pnqvisibility = 'hidden'
  efvisibility = 'hidden'
  gvisibility = 'hidden'


  signupRequestPayload: SignupRequestPayload;

  signupForm = new FormGroup({
    firstName: new FormControl('', Validators.required),
    lastName: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required),
    passwordConfirm: new FormControl('', Validators.required)


  })

  constructor(private authService: AuthService, private router: Router,
    private toastr: ToastrService) {

    this.signupRequestPayload = {
      firstName: '',
      lastName: '',
      email: '',
      password: ''
    };


  }

  ngOnInit(): void {
    document.body.className = "selector";
  }

  
  signup() {

    this.pnqvisibility = 'hidden'
    this.efvisibility = 'hidden'
    this.gvisibility = 'hidden'


    if (this.signupForm.get('email')!.value?.length == 0 
      || this.signupForm.get('firstName')!.value?.length ==  0
      || this.signupForm.get('lastName')!.value?.length ==  0
      || this.signupForm.get('password')!.value?.length == 0 
      || this.signupForm.get('passwordConfirm')!.value?.length == 0  ) {
      console.error("empty field!");
      this.efvisibility = 'visible'
    }
    else if (this.signupForm.get('password')!.value != this.signupForm.get('passwordConfirm')!.value) {
      console.error("passwords not equal");
      this.pnqvisibility = 'visible'
    }
    else {
      this.signupRequestPayload.email = this.signupForm.get('email')!.value;
      this.signupRequestPayload.firstName = this.signupForm.get('firstName')!.value;
      this.signupRequestPayload.lastName = this.signupForm.get('lastName')!.value;
      this.signupRequestPayload.password = this.signupForm.get('password')!.value;

      this.authService.signup(this.signupRequestPayload)
        .subscribe(
          data => {
          this.router.navigate([''],
            { queryParams: { registered: 'true' } });
        }, error => {
          console.log(error);
          this.gvisibility = 'visible'
          this.toastr.error('Registration Failed! Please try again');
        });
    }

  }
}
