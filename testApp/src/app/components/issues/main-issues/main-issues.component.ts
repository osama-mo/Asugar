import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-main-issues',
  templateUrl: './main-issues.component.html',
  styleUrls: ['./main-issues.component.css']
})
export class MainIssuesComponent {
  issues = [{
    id: 3,
    title: "third issue",
    des: "hey i am the third issue",
    manHour: 5,
    condition: "TODO",
    issueType: "STORY",
    creator: "enes@gmail.com",
    assigned: "osama.@gmail.com",
    createdAt: "2022-12-30"
  }, {
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
  projectId: String | null = "";
  projectName: String | null = "";

  constructor(private route: ActivatedRoute, private router: Router) {
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');
  }

  ngOnInit(): void {

  }

  navigateToActiveSprint() {
    this.router.navigate(['active-sprint'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToMembers() {
    this.router.navigate(['memberslist'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
}
