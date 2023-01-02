import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'app/components/auth/shared/auth.service';
import { IssueResponsePayload } from './issue-respone-payload';

@Component({
  selector: 'app-active-sprint',
  templateUrl: './active-sprint.component.html',
  styleUrls: ['./active-sprint.component.css']
})
export class ActiveSprintComponent {
  projectId: string | null = "";
  projectName: string | null = "";
  todo: IssueResponsePayload[] = [

  ];
  inProgress: IssueResponsePayload[] = [

  ]
  done: IssueResponsePayload[] = [

  ];
  issues: IssueResponsePayload[] = [
  ]
  drop(event: CdkDragDrop<any>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }
    for (let issue of this.inProgress) {
      if (issue.condition != "IN_PROGRESS") {
        this.authsurvice.setCondition(Number(this.projectId), issue.id, "IN_PROGRESS").subscribe(
          data => {
            console.log(data)
           },
            error => {
              new Error(error)
        });
      }
    }
    for (let issue of this.done) {
      if (issue.condition != "DONE") {
        this.authsurvice.setCondition(Number(this.projectId), issue.id, "DONE").subscribe(
          data => {
            console.log(data)
           },
            error => {
              new Error(error)
        });
      }
    }
    for (let issue of this.todo) {
      if (issue.condition != "TODO") {
        this.authsurvice.setCondition(Number(this.projectId), issue.id, "TODO").subscribe(
          data => {
            console.log(data)
           },
            error => {
              new Error(error)
        });
      }
    }
  }
  constructor(private route: ActivatedRoute, private router: Router, private authsurvice: AuthService) {
    document.body.className = "selector";
  }

  ngOnInit(): void {
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');

    this.authsurvice.getIssues(this.projectId!).subscribe(
      data => {
        console.log(data)
        this.issues = data
        for (let issue of this.issues) {
          if (issue.sprint == "active") {
            if (issue.condition == "TODO") {
              this.todo.push(issue)
            }
            else if (issue.condition == "IN_PROGRESS") {
              this.inProgress.push(issue)
            }
            else if (issue.condition == "DONE") {
              this.done.push(issue)
            }
          }
        }
      }
      , error => {
        new Error(error)
      })

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
