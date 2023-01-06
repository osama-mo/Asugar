import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { throwError } from 'rxjs';
import { AuthService } from '../shared/auth.service';
import { ForgotMyPasswordRequestPayload } from './forgot-my-password-request-payload';

@Component({
  selector: 'app-forgot-my-password',
  templateUrl: './forgot-my-password.component.html',
  styleUrls: ['./forgot-my-password.component.css']
})
export class ForgotMyPasswordComponent implements OnInit {

  efvisibility = 'hidden'

  isError!: boolean;

  fgmForm = new FormGroup({

    email: new FormControl('', [Validators.required, Validators.email]),

  })

  constructor(private authService: AuthService, private router: Router,private toastr: ToastrService) {
    this.fgmRequestPayload = {
      email: '',
    };
  }
  fgmRequestPayload: ForgotMyPasswordRequestPayload;

  ngOnInit(): void {
    document.body.className = "selector";
  }
  forgotpassword() {

    this.efvisibility = 'hidden'

   
      this.fgmRequestPayload.email = this.fgmForm.get('email')!.value;
      this.authService.forgetMyPassword(this.fgmRequestPayload).subscribe(data => {
      }, error => {

        this.isError = true;
        throwError(error);
        this.toastr.error("Some thing went wrong","Erorr")
      });

      if (this.isError != true) {
        this.router.navigate(['forgot-my-password-confirmation']);
      }
    }

  
}
