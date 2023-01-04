import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'app/components/auth/shared/auth.service';

@Component({
  selector: 'app-members-list',
  templateUrl: './members-list.component.html',
  styleUrls: ['./members-list.component.css']
})
export class MembersListComponent implements OnInit {
  projectId: String | null = "";
  projectName: String | null = "";

  members = [
    {
      last_name: "",
      title: '',
      first_name: '',
      email: ''
    }
  ]
  constructor(private route: ActivatedRoute, private router: Router, private authsurvice: AuthService) {

  }

  ngOnInit(): void {
    document.body.className = "selector";
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');

    this.authsurvice.getMembersList(Number(this.projectId)).subscribe(
      data => {
        console.log(data)
        this.members = data
      }
      , error => {
        new Error(error)
      })
  }

  navigateToAddmember() {
    this.router.navigate(['add-member'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }

  navigateToMemberDetails(memberemail: string, membertitle: string, memberfn: string, memberln: string) {
    this.router.navigate(['member-details'], { queryParams: { projectId: this.projectId, projectName: this.projectName, email: memberemail, title: membertitle, fn: memberfn, ln: memberln } })
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
}
