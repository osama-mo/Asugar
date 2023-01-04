import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {Location} from '@angular/common'
import { AuthService } from 'app/components/auth/shared/auth.service';
import { CreateEpicRequestPayload } from './create-epic-reques-payload';

@Component({
  selector: 'app-create-epic',
  templateUrl: './create-epic.component.html',
  styleUrls: ['./create-epic.component.css']
})
export class CreateEpicComponent {
  createEpicRequset: CreateEpicRequestPayload;
  efvisibility = 'hidden'
  gvisibility = 'hidden'



  createEpicForm = new FormGroup({
    title: new FormControl('', Validators.required),
    des: new FormControl('', [Validators.required]),
    manHour: new FormControl('', [Validators.required]),
    due: new FormControl('', [Validators.required]),
    assignedTo: new FormControl('', [Validators.required]),
  })
  projectId: string | null;
  projectName: string | null;
  members = [
    {
      last_name: "",
      title: '',
      first_name: '',
      email: ''
    }
  ]


  constructor(private route: ActivatedRoute, private router: Router, private authService: AuthService,private location: Location) {
    
    this.createEpicRequset = {
      title: "",
      description: "",
      manHour: null,
      assignedTo: "",
      due:null
    }
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');
  }

  ngOnInit(): void {
    document.body.className = "selector";
    this.authService.getMembersList(Number(this.projectId)).subscribe(
      data => {
        console.log(data)
        this.members = data
      }
      , error => {
        new Error(error)
      })
  }


  createEpic() {
    this.efvisibility = 'hidden'
    this.gvisibility = 'hidden'
    this.createEpicRequset.title = this.createEpicForm.get('title')!.value;
    this.createEpicRequset.description = this.createEpicForm.get('des')!.value;
    this.createEpicRequset.manHour = Number(this.createEpicForm.get('manHour')!.value);
    
    this.authService.createEpic( Number(this.projectId!),this.createEpicRequset,)
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
