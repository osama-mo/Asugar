import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import { AuthService } from '../shared/auth.service';
import { ForgotMyPasswordConfirmationRequestPayload } from './forgot-my-password-confirmation-request.payload';

@Component({
  selector: 'app-forgot-my-password-confirmation',
  templateUrl: './forgot-my-password-confirmation.component.html',
  styleUrls: ['./forgot-my-password-confirmation.component.css']
})
export class ForgotMyPasswordConfirmationComponent implements OnInit {
  isError!: boolean;

  pnqvisibility = 'hidden'
  efvisibility = 'hidden'
  gvisibility = 'hidden'

  fgmcForm = new FormGroup({

    confirmationCode: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]),
    passwordConfirm: new FormControl('',[Validators.required])

  })

  constructor(private authService: AuthService, private router: Router) {
    this.fgmcRequestPayload = {
      token: '',
      password: '',
    };
  }
  fgmcRequestPayload: ForgotMyPasswordConfirmationRequestPayload;

  ngOnInit(): void {
    document.body.className = "selector";
  }

  forgotMyPasswordConfirmation() {


    this.pnqvisibility = 'hidden'
    this.efvisibility = 'hidden'
    this.gvisibility = 'hidden'

    if (this.fgmcForm.get('confirmationCode')!.value?.length == 0 
      || this.fgmcForm.get('password')!.value?.length == 0 
      || this.fgmcForm.get('passwordConfirm')!.value?.length == 0  ) {
      console.error("empty field!");
      this.efvisibility = 'visible'
    }
    else if (this.fgmcForm.get('password')!.value != this.fgmcForm.get('passwordConfirm')!.value) {
      console.error("passwords not equal");
      this.pnqvisibility = 'visible'
    }
    else {
    this.fgmcRequestPayload.token = this.fgmcForm.get('confirmationCode')!.value;
    this.fgmcRequestPayload.password = this.fgmcForm.get('password')!.value;
    this.authService.forgetMyPasswordConfirmation(this.fgmcRequestPayload).subscribe(data => {
    }, error => {

      this.isError = true;
      throwError(error);
    });
    if (this.isError != true) {
      this.router.navigate(['']);
    }
  }
}

}
