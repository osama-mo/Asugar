import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'app/components/auth/shared/auth.service';
import { CreateIssueRequestPayload } from './create-issue-request-payload';

@Component({
  selector: 'app-create-issue',
  templateUrl: './create-issue.component.html',
  styleUrls: ['./create-issue.component.css']
})
export class CreateIssueComponent {
  createIssueRequset: CreateIssueRequestPayload;
  efvisibility = 'hidden'
  gvisibility = 'hidden'


  
  addMemberForm = new FormGroup({
    title: new FormControl('', Validators.required),
    des: new FormControl('', [Validators.required]),
    epicId: new FormControl('', [Validators.required]),
    IssueType: new FormControl('', [Validators.required]),
    manHour: new FormControl('', [Validators.required]),
    sprint: new FormControl('', [Validators.required]),
  })

  
  constructor( private authService : AuthService) {
    this.createIssueRequset = {
      title : "",
      description : "",
      epicId: null,
      issueType : "",
      manHour: null,
      sprint: null
    }
   }

  ngOnInit(): void {
  }


  createIssue(){


    this.efvisibility = 'hidden'
    this.gvisibility = 'hidden'

      this.authService.createIssue(this.createIssueRequset)
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
