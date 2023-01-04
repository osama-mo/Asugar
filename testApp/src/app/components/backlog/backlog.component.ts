import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../auth/shared/auth.service';
import { IssueResponsePayload } from '../sprints/active-sprint/issue-respone-payload';

@Component({
  selector: 'app-backlog',
  templateUrl: './backlog.component.html',
  styleUrls: ['./backlog.component.css']
})
export class BacklogComponent implements OnInit {
  projectId: string | null = "";
  projectName: string | null = "";

  activeSprint: IssueResponsePayload[] = [];
  nextSprint: IssueResponsePayload[] = [];
  Backlog: IssueResponsePayload[] = [];
  issues: IssueResponsePayload[] = [];

  constructor(private route: ActivatedRoute, private router: Router, private authsurvice: AuthService) {
  }

  ngOnInit(): void {
    document.body.className = "selector";
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');

    this.authsurvice.getIssues(this.projectId!).subscribe(
      data => {
        console.log(data)
        this.issues = data
        for (let issue of this.issues) {

          if (issue.sprint == "active") {

            this.activeSprint.push(issue)
          }
          else if (issue.sprint == "next") {
            this.nextSprint.push(issue)
          }
          else {
            this.Backlog.push(issue)
          }
        }
      }
      , error => {
        new Error(error)
      })
  }
  drop(event: CdkDragDrop<any[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
      for (let issue of this.Backlog) {
        if (issue.sprint != null) {
          // setSprint
        }
      }
      for (let issue of this.activeSprint) {
        if (issue.sprint != "active") {
          // setSprint
        }
      }
      for (let issue of this.nextSprint) {
        if (issue.sprint != "next") {
          // setSprint
        }
      }
    }
  }

  finishSprint(){
    this.authsurvice.finishSprint(this.projectId!).subscribe(
      data => {
        console.log(data)
        window.location.reload()
        
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
  navigateToEpics() {
    this.router.navigate(['epics-list'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
}
