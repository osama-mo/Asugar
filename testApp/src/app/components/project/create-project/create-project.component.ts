import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'app/components/auth/shared/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-create-project',
  templateUrl: './create-project.component.html',
  styleUrls: ['./create-project.component.css']
})
export class CreateProjectComponent implements OnInit {

  isError: boolean = false;
  efvisibility = 'hidden'
  gvisibility = 'hidden'
  createProjectForm = new FormGroup({
    
    projectName: new FormControl('', [Validators.required]),
    
  })

  projectName:string| null = "";
  
  constructor(private authService: AuthService, private router: Router,private toastr: ToastrService) { }

  ngOnInit(): void {
    document.body.className = "selector";
  }

  createProject(){
    this.efvisibility = 'hidden'
    if(this.createProjectForm.get('projectName')!.value?.length == 0){
      this.efvisibility = 'visible'
    }
    else{
      this.projectName =this.createProjectForm.get('projectName')!.value ;
      this.authService.createProject(this.projectName).subscribe(data => {
        this.router.navigate(['list-project']);
      },
      error => {
      console.log(error);
      this.isError = true;
      this.gvisibility = 'visible'
      this.toastr.error(error.error, "Error");
    });
    }
  
}
}
