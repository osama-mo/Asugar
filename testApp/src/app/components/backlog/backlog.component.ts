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

  activeSprint = [
    {
      id: 2,
      title: "sec issue",
      des: "hey i am the sec issue",
      manHour: 7,
      epicId: null,
      sprint: null,
      condition: "in progress",
      issueType: "STORY",
      creator: "enes@gmail.com",
      assigned: "osama.@gmail.com",
      createdAt: "2022-12-30"
    },
  ];
  nextSprint  = [
    {
      id: 2,
      title: "sec issue",
      des: "hey i am the sec issue",
      manHour: 7,
      epicId: null,
      sprint: null,
      condition: "in progress",
      issueType: "STORY",
      creator: "enes@gmail.com",
      assigned: "osama.@gmail.com",
      createdAt: "2022-12-30"
    },
  ]
  Backlog  = [
    {
      id: 2,
      title: "sec issue",
      des: "hey i am the sec issue",
      manHour: 7,
      epicId: null,
      sprint: null,
      condition: "in progress",
      issueType: "STORY",
      creator: "enes@gmail.com",
      assigned: "osama.@gmail.com",
      createdAt: "2022-12-30"
    },
  ];
  issues = [
    {
      id: 2,
      title: "sec issue",
      des: "hey i am the sec issue",
      manHour: 7,
      condition: "in progress",
      issueType: "STORY",
      creator: "enes@gmail.com",
      assigned: "osama.@gmail.com",
      createdAt: "2022-12-30"
    }
  ]
  drop(event: CdkDragDrop<any[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else { 
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
        for(let issue of this.Backlog){
          if(issue.sprint != null)
          {
            // setSprint
          }
        }
        for(let issue of this.activeSprint){
          if(issue.sprint != "active")
          {
            // setSprint
          }
        }
        for(let issue of this.nextSprint){
          if(issue.sprint != "next")
          {
            // setSprint
          }
        }
    }
  }
  constructor(private route: ActivatedRoute,private router: Router,private authsurvice: AuthService) { 
  }

  ngOnInit(): void {
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

  navigateToActiveSprint(){
    this.router.navigate(['active-sprint'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToMembers() {
    this.router.navigate(['memberslist'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToIssues(){
    this.router.navigate(['issues'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
}
