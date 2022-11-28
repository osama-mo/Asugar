import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { throwError } from 'rxjs';
import { AuthService } from '../shared/auth.service';
import { ForgotMyPasswordRequestPayload } from './forgot-my-password-request-payload';

@Component({
  selector: 'app-forgot-my-password',
  templateUrl: './forgot-my-password.component.html',
  styleUrls: ['./forgot-my-password.component.css']
})
export class ForgotMyPasswordComponent implements OnInit {
  fgmForm = new FormGroup({
    
    email: new FormControl('', [Validators.required, Validators.email]),
    
  })

  constructor(private authService: AuthService,) { 
    this.fgmRequestPayload = {
      email: '',
    };
  }
  fgmRequestPayload: ForgotMyPasswordRequestPayload;
  
  ngOnInit(): void {
  }
  forgotpassword() {
    this.fgmRequestPayload.email = this.fgmForm.get('email')!.value;

    // this.authService.forgetMyPassword(this.fgmRequestPayload).subscribe(data => {
     
    // }, error => {
    //   this.isError = true;
    //   throwError(error);
    // });
  }
}
