import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'app/components/auth/shared/auth.service';

@Component({
  selector: 'app-epic-list',
  templateUrl: './epic-list.component.html',
  styleUrls: ['./epic-list.component.css']
})
export class EpicListComponent {
  epics = [ {
    id: 0,
    title: "first",
    description: "first",
    manHour: 0,
    issueType: "TODO",
    creatorUsername: "m.erkmen2019@gmail.calsşl",
    assignedTo: "m.erkmen2019@gmail.calsşl",
    createdAt: "22.22.2222",
    endedAt: "22.22.2222",
    plannedTo: "22.22.2222"
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

    this.authsurvice.getEpics(Number(this.projectId!)).subscribe(
      data => {
        console.log(data)
        this.epics = data
      }
      , error => {
        new Error(error)
      })
  }

  
 
 
  navigateToRemoveEpic() {
    this.router.navigate(['delete-epic'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToSubIssues(epicId: number,epicName:string,endedAt:string|null){
    let state;
    if(endedAt == null){
      state = "Not"
    }
    else{
      state = "finished"
    }
    this.router.navigate(['sub-issues-list'], { queryParams: { state:state,epicName:epicName,epicId: epicId, projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToAddEpic() {
    this.router.navigate(['create-epic'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
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

  getShortDate(date: string|null) { 
    let shortdate = "";
    let n = 0;
    for(let letter of date!){
      shortdate = shortdate + letter
      n++
      if(n== 10){
        break
      }
    }
    return shortdate;
  }
}
