import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'app/components/auth/shared/auth.service';
import { AddMemberRequestPayload } from './add-member-request-payload';

@Component({
  selector: 'app-add-member',
  templateUrl: './add-member.component.html',
  styleUrls: ['./add-member.component.css']
})
export class AddMemberComponent implements OnInit {

  addMemberRequest: AddMemberRequestPayload ;
  efvisibility = 'hidden'
  gvisibility = 'hidden'

  addMemberForm = new FormGroup({
    title: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
  })

  
  constructor( private authService : AuthService) {
    this.addMemberRequest = {
      email : "",
      title : ""
    }
   }

  ngOnInit(): void {
  }


  addMember(){

    this.efvisibility = 'hidden'
    this.gvisibility = 'hidden'


    if (this.addMemberForm.get('email')!.value?.length == 0 
      || this.addMemberForm.get('title')!.value?.length ==  0
        ) {
      console.error("empty field!");
      this.efvisibility = 'visible'
    }
    else {
      this.addMemberRequest.email = this.addMemberForm.get('email')!.value;
      this.addMemberRequest.title = this.addMemberForm.get('title')!.value;

      // this.authService.addMember(this.signupRequestPayload)
      //   .subscribe(
      //     data => {
      //     this.router.navigate([''],
      //       { queryParams: { registered: 'true' } });
      //   }, error => {
      //     console.log(error);
      //     this.gvisibility = 'visible'
      //     this.toastr.error('Registration Failed! Please try again');
      //   });
    }
  }
}
