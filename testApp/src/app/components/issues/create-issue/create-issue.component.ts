import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Location } from '@angular/common'
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'app/components/auth/shared/auth.service';
import { CreateIssueRequestPayload } from './create-issue-request-payload';

@Component({
  selector: 'app-create-issue',
  templateUrl: './create-issue.component.html',
  styleUrls: ['./create-issue.component.css']
})
export class CreateIssueComponent {
  createIssueRequset: CreateIssueRequestPayload;
  efvisibility = 'hidden'
  gvisibility = 'hidden'



  createIssueForm = new FormGroup({
    title: new FormControl('', Validators.required),
    des: new FormControl('', [Validators.required]),
    epicId: new FormControl('', [Validators.required]),
    IssueType: new FormControl('', [Validators.required]),
    
    manHour: new FormControl('', [Validators.required]),
    assignedTo: new FormControl('',[Validators.required]),
    sprint: new FormControl('', [Validators.required]),
    
  })
  projectId: string | null;
  projectName: string | null;


  constructor(private route: ActivatedRoute, private router: Router, private authService: AuthService,private location: Location) {
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
  }

  ngOnInit(): void {

  }


  createIssue() {
    this.efvisibility = 'hidden'
    this.gvisibility = 'hidden'
    this.createIssueRequset.title = this.createIssueForm.get('title')!.value;
    this.createIssueRequset.description = this.createIssueForm.get('des')!.value;
    this.createIssueRequset.epicId = this.createIssueForm.get('epicId')!.value;
    this.createIssueRequset.issueType = this.createIssueForm.get('IssueType')!.value;
    this.createIssueRequset.manHour = this.createIssueForm.get('manHour')!.value;
    this.createIssueRequset.assignedTo = this.createIssueForm.get('assignedTo')!.value;
    this.createIssueRequset.sprint = this.createIssueForm.get('sprint')!.value;


    this.authService.createIssue(this.createIssueRequset, this.projectId!)
      .subscribe(
        data => {
          this.location.back();
        }, error => {
          console.log(error);
          this.gvisibility = 'visible'
        });

  }
}
