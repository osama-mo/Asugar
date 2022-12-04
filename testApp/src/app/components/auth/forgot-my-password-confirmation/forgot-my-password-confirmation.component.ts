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

  fgmcForm = new FormGroup({
    
    confirmationCode: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required]), 
    
  })

  constructor(private authService: AuthService, private router: Router) {
    this.fgmcRequestPayload = {
      token: '',
      password: ''
    };
   }
   fgmcRequestPayload: ForgotMyPasswordConfirmationRequestPayload;

  ngOnInit(): void {
  }

  forgotMyPasswordConfirmation(){
    this.fgmcRequestPayload.token = this.fgmcForm.get('confirmationCode')!.value;
    this.fgmcRequestPayload.password = this.fgmcForm.get('password')!.value;
    this.authService.forgetMyPasswordConfirmation(this.fgmcRequestPayload).subscribe(data => {    
    }, error => {
      
      this.isError = true;
      throwError(error);
    });
    if(this.isError != true){
      this.router.navigate(['forgot-my-password-confirmation']);
    }
  }

}
