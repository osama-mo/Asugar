import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {Location} from '@angular/common'
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'app/components/auth/shared/auth.service';
import { CreateIssueRequestPayload } from './create-issue-request-payload';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-create-sub-issue',
  templateUrl: './create-sub-issue.component.html',
  styleUrls: ['./create-sub-issue.component.css']
})
export class CreateSubIssueComponent {
  createIssueRequset: CreateIssueRequestPayload;
  efvisibility = 'hidden'
  gvisibility = 'hidden'



  createIssueForm = new FormGroup({
    title: new FormControl('', Validators.required),
    des: new FormControl('', [Validators.required]),
    IssueType: new FormControl('', [Validators.required]),
    manHour: new FormControl('', [Validators.required]),
    assignedTo: new FormControl('',[Validators.required]),
    sprint: new FormControl('', [Validators.required]),
    
  })
  projectId: string | null;
  projectName: string | null;
  epicId: string | null;
  members = [
    {
      last_name: "",
      title: '',
      first_name: '',
      email: ''
    }
  ]


  constructor(private route: ActivatedRoute, private router: Router, private authService: AuthService,private location: Location,private toastr: ToastrService) {
    this.createIssueRequset = {
      title: "",
      description: "",
      epicId: null,
      issueType: "",
      manHour: null,
      assignedTo: "",
      sprint: null
    }
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');
    this.epicId = this.route.snapshot.queryParamMap.get('epicId');
  }

  ngOnInit(): void {
    document.body.className = "selector";
    this.authService.getMembersList(Number(this.projectId)).subscribe(
      data => {
        console.log(data)
        this.members = data
      }
      , error => {
        this.toastr.error(error.error,"Error");
      })
  }


  createIssue() {
    this.efvisibility = 'hidden'
    this.gvisibility = 'hidden'
    this.createIssueRequset.title = this.createIssueForm.get('title')!.value;
    this.createIssueRequset.description = this.createIssueForm.get('des')!.value;
    this.createIssueRequset.epicId = this.epicId;
    this.createIssueRequset.issueType = this.createIssueForm.get('IssueType')!.value;
    this.createIssueRequset.manHour = Number(this.createIssueForm.get('manHour')!.value);
    this.createIssueRequset.assignedTo = this.createIssueForm.get('assignedTo')!.value;
    this.createIssueRequset.sprint = this.createIssueForm.get('sprint')!.value;
    console.log(this.createIssueForm.get('assignedTo')!.value)

    this.authService.createIssue(this.createIssueRequset, this.projectId!)
      .subscribe(
        data => {
          this.location.back();
        }, error => {
          console.log(error);
          this.toastr.error(error.error,"Error");
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
