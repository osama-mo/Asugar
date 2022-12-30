import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component } from '@angular/core';
import { IssueResponsePayload } from './issue-respone-payload';

@Component({
  selector: 'app-active-sprint',
  templateUrl: './active-sprint.component.html',
  styleUrls: ['./active-sprint.component.css']
})
export class ActiveSprintComponent {
  todo:IssueResponsePayload[] = [
    {
      issueId: "EPIC-2",
      issueDescription: "Task distribution and planning"
    },
  ];
  inProgress :IssueResponsePayload[] = [
    {
      issueId: "TASK-1",
      issueDescription: "Frontend developers test the backend"
    },
    {
      issueId: "STORY-2",
      issueDescription: "Add project function"
    },
    
  ]
  done :IssueResponsePayload[] = [
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
    console.log("todo:")
    this.todo.forEach((item)=> console.log(item.issueId))
    console.log("in p:")
    this.inProgress.forEach((item)=> console.log(item.issueId))
    console.log("done:")
    this.done.forEach((item)=> console.log(item.issueId))
    console.log(" ")
    
  }
  constructor() { 
    document.body.className = "selector";
  }

  ngOnInit(): void {
  }

}
