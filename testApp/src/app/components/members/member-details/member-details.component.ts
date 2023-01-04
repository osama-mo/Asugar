import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'app/components/auth/shared/auth.service';
import { MemberDetailsResponse } from './member-details-response';

@Component({
  selector: 'app-member-details',
  templateUrl: './member-details.component.html',
  styleUrls: ['./member-details.component.css']
})
export class MemberDetailsComponent implements OnInit {

  projectId: String | null = "";
  projectName: String | null = "";

  memberdetails: MemberDetailsResponse;


  constructor(private route: ActivatedRoute, private router: Router, private authsurvice: AuthService) {
    document.body.className = "selector";
    this.memberdetails = {
      firstName: "",
      Email: "",
      LastName: "",
      title: ""
    }
    
  }


  ngOnInit(): void {
    this.memberdetails.Email = this.route.snapshot.queryParamMap.get('email');
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');
    this.memberdetails.firstName = this.route.snapshot.queryParamMap.get('fn');
    this.memberdetails.LastName= this.route.snapshot.queryParamMap.get('ln');
    this.memberdetails.title = this.route.snapshot.queryParamMap.get('title');
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
  navigateToEpics() {
    this.router.navigate(['epics-list'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }

  removemember(){
    this.authsurvice.removeMember(Number(this.projectId!),this.memberdetails.Email!)
        .subscribe(
          data => {
            console.log(data);
        }, error => {
          console.log(error);
        });
    }
  }


