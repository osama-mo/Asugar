import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'app/components/auth/shared/auth.service';
import {Location} from '@angular/common'

@Component({
  selector: 'app-delete-sub-issue',
  templateUrl: './delete-sub-issue.component.html',
  styleUrls: ['./delete-sub-issue.component.css']
})
export class DeleteSubIssueComponent {
  efvisibility = 'hidden'
  gvisibility = 'hidden'


  
  removeIssueForm = new FormGroup({
    id: new FormControl('', Validators.required),
    
  })
  projectId: string | null;
  issueId: string | null = "";
  projectName: string | null;

  
  constructor(private route: ActivatedRoute, private router: Router, private authService : AuthService ,private location: Location) {
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');
   }

  ngOnInit(): void {
    document.body.className = "selector";
  }


  removeIssue(){
    this.efvisibility = 'hidden'
    this.gvisibility = 'hidden'
    this.issueId= this.removeIssueForm.get('id')!.value;
    this.authService.removeIssue(this.issueId!,this.projectId!)
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
