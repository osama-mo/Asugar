import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common'
import { AuthService } from 'app/components/auth/shared/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-issues-list',
  templateUrl: './issues-list.component.html',
  styleUrls: ['./issues-list.component.css']
})
export class IssuesListComponent {
  issues = [{
    id: 0,
    title: "",
    description: "",
    manHour: 0,
    condition: "",
    issueType: "",
    creatorUsername: "",
    assignedTo: "",
    createdAt: ""
  }]
  projectId: string | null = "";
  projectName: String | null = "";
  epicId: string | null = "";
  state: string | null = "";
  finished: boolean = false;
  epicName: string | null = "";

  constructor(private route: ActivatedRoute, private router: Router, private authsurvice: AuthService, private toastr: ToastrService, private location: Location) {
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');
    this.epicId = this.route.snapshot.queryParamMap.get('epicId');
    this.epicName = this.route.snapshot.queryParamMap.get('epicName');
    this.state = this.route.snapshot.queryParamMap.get('state');
    if (this.state == "finished") {
      this.finished = true
    }
    else {
      this.finished = false
    }
  }

  ngOnInit(): void {
    document.body.className = "selector";

    this.authsurvice.getEpicIssues(Number(this.projectId!), Number(this.epicId!)).subscribe(
      data => {
        console.log(data)
        this.issues = data
      }
      , error => {
        new Error(error)
        this.toastr.error(error.error, "Error");
      })
  }




  navigateToRemoveIssue() {
    this.router.navigate(['delete-sub-issue'], { queryParams: { projectId: this.projectId, projectName: this.projectName } })
  }
  navigateToAddIssue() {
    this.router.navigate(['create-sub-issue'], { queryParams: { epicId: this.epicId, projectId: this.projectId, projectName: this.projectName } })
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

  finishEpic() {
    this.authsurvice.finishEpic(Number(this.epicId!), Number(this.projectId!)).subscribe(
      data => {
        console.log(data)
        this.location.back()
      }
      , error => {
        new Error(error)
        this.toastr.error(error.error, "Error");
      })
  }
  getShortDate(date: string) {
    let shortdate = "";
    let n = 0;
    for (let letter of date) {
      shortdate = shortdate + letter
      n++
      if (n == 10) {
        break
      }
    }
    return shortdate;
  }
}
