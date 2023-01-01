import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IssueResponsePayload } from '../sprints/active-sprint/issue-respone-payload';

@Component({
  selector: 'app-backlog',
  templateUrl: './backlog.component.html',
  styleUrls: ['./backlog.component.css']
})
export class BacklogComponent implements OnInit {
  projectId: String | null = "";
  projectName: String | null = "";
  activeSprint:IssueResponsePayload[] = [
    {
      issueId: "EPIC-2",
      issueDescription: "Task distribution and planning"
      
    },

  ];
  nextSprint :IssueResponsePayload[] = [
    {
      issueId: "TASK-1",
      issueDescription: "Frontend developers test the backend"
    },
    {
      issueId: "STORY-2",
      issueDescription: "Add project function"
    },
    
  ]
  Backlog :IssueResponsePayload[] = [
    {
      issueId: "Epic-1",
      issueDescription: "Core design implementation"
    },
    {
      issueId: "STORY-1",
      issueDescription: "Decide on a design theme"
    },
    {
      issueId: "TASK-2",
      issueDescription: "Log in page"
    },
  ];
  issuesList = ['root', 'child1', 'child2']
  drop(event: CdkDragDrop<IssueResponsePayload[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else { 
      transferArrayItem(event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex);
    }
  }
  constructor(private route: ActivatedRoute,private router: Router) { 
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');
  }

  ngOnInit(): void {
    
  }

  navigateToActiveSprint(){
    this.router.navigate(['active-sprint'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToMembers() {
    this.router.navigate(['memberslist'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }

}
