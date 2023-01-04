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
    id: 2,
    title: "sec issue",
    description: "hey i am the sec issue",
    manHour: 7,
    issueType: "STORY",
    creatorUsername: "enes@gmail.com",
    assignedTo: "osama.@gmail.com",
    createdAt: "2012-01-01",
    endedAt: "2012-01-01",
    plannedTo: "2012-01-01"
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
  navigateToSubIssues(epicId: number,epicName:string){
    this.router.navigate(['sub-issues-list'], { queryParams: { epicName:epicName,epicId: epicId, projectId: this.projectId, projectName: this.projectName } })
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

  getShortDate(date: string) { 
    let shortdate = "";
    let n = 0;
    for(let letter of date){
      shortdate = shortdate + letter
      n++
      if(n== 10){
        break
      }
    }
    return shortdate;
  }
}
