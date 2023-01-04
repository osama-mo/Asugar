import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {Location} from '@angular/common'
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'app/components/auth/shared/auth.service';

@Component({
  selector: 'app-assign-to-epic',
  templateUrl: './assign-to-epic.component.html',
  styleUrls: ['./assign-to-epic.component.css']
})
export class AssignToEpicComponent {
  efvisibility = 'hidden'
  gvisibility = 'hidden'



  assignToEpicForm = new FormGroup({
    issueId : new FormControl('', [Validators.required]),
    epicId: new FormControl('', [Validators.required]),  
  })
  projectId: string | null;
  issueId: string | null;
  epicId: string | null;
  projectName: string | null;


  constructor(private route: ActivatedRoute, private router: Router, private authService: AuthService,private location: Location) {
    this.epicId = "";
    this.issueId = "";
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');
  }

  ngOnInit(): void {
    document.body.className = "selector";
  }


  assign() {
    this.efvisibility = 'hidden'
    this.gvisibility = 'hidden'
    this.epicId = this.assignToEpicForm.get('epicId')!.value;
    this.issueId = this.assignToEpicForm.get('issueId')!.value;
    this.authService.assignIssueToEpic(Number(this.projectId!),Number(this.issueId!),Number(this.epicId))
      .subscribe(
        data => {
          this.location.back();
        }, error => {
          console.log(error);
          this.gvisibility = 'visible'
        });

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
