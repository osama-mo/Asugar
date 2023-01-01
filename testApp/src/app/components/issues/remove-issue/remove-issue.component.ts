import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common'
import { AuthService } from 'app/components/auth/shared/auth.service';

@Component({
  selector: 'app-remove-issue',
  templateUrl: './remove-issue.component.html',
  styleUrls: ['./remove-issue.component.css']
})
export class RemoveIssueComponent {

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
  
}
