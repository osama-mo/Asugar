import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'app/components/auth/shared/auth.service';

@Component({
  selector: 'app-main-issues',
  templateUrl: './main-issues.component.html',
  styleUrls: ['./main-issues.component.css']
})
export class MainIssuesComponent {
  issues = [ {
    id: 2,
    title: "sec issue",
    des: "hey i am the sec issue",
    manHour: 7,
    condition: "in progress",
    issueType: "STORY",
    creator: "enes@gmail.com",
    assigned: "osama.@gmail.com",
    createdAt: "2022-12-30"
  }]
  projectId: string | null = "";
  projectName: String | null = "";

  constructor(private route: ActivatedRoute, private router: Router, private authsurvice: AuthService) {
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');
  }

  ngOnInit(): void {
    document.body.className = "selector";
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');

    this.authsurvice.getIssues(this.projectId!).subscribe(
      data => {
        console.log(data)
        this.issues = data
      }
      , error => {
        new Error(error)
      })
  }

  
  navigateToActiveSprint() {
    this.router.navigate(['active-sprint'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToMembers() {
    this.router.navigate(['memberslist'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToRemoveIssue() {
    this.router.navigate(['remove-issue'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToAddIssue() {
    this.router.navigate(['create-issue'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
}
