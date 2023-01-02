import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'app/components/auth/shared/auth.service';
import{ Location} from '@angular/common'
import { AddMemberRequestPayload } from './add-member-request-payload';

@Component({
  selector: 'app-add-member',
  templateUrl: './add-member.component.html',
  styleUrls: ['./add-member.component.css']
})
export class AddMemberComponent implements OnInit {

  projectId: String | null = "";
  projectName: String | null = "";

  addMemberRequest: AddMemberRequestPayload ;
  efvisibility = 'hidden'
  gvisibility = 'hidden'

  addMemberForm = new FormGroup({
    title: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
  })

  
  constructor(private location:Location ,private route: ActivatedRoute, private router: Router, private authService: AuthService) {
    this.addMemberRequest = {
      email : "",
      title : ""
    }
   }

  ngOnInit(): void {
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');
  }


  addMember(){

    this.efvisibility = 'hidden'
    this.gvisibility = 'hidden'


    if (this.addMemberForm.get('email')!.value?.length == 0 ) {
      console.error("empty field!");
      this.gvisibility = 'visible'
    }
    else {
      this.addMemberRequest.email = this.addMemberForm.get('email')!.value;
      this.addMemberRequest.title = this.addMemberForm.get('title')!.value;

      this.authService.addMember(this.projectId!,this.addMemberRequest.email!)
        .subscribe(
          data => {
            this.location.back()
        }, error => {
          console.log(error);
          this.gvisibility = 'visible'
        });
    }
  }

  navigateToProjects() {
    this.router.navigate(['list-project'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToBacklog() {
    this.router.navigate(['backlog'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToSprint() {
    this.router.navigate(['active-sprint'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToMembers() {
    this.router.navigate(['memberslist'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToIssues() {
    this.router.navigate(['issues'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
}
