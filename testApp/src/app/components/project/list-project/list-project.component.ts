import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from 'app/components/auth/shared/auth.service';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-list-project',
  templateUrl: './list-project.component.html',
  styleUrls: ['./list-project.component.css']
})
export class ListProjectComponent implements OnInit {
  projects = [
    {
      name: "blablabla",
      id: 123
    }
  ]


  constructor(private router: Router, private authsurvice: AuthService) {


    authsurvice.getProjectlist().subscribe(data => {
      console.log(data)
      this.projects = data
    }
      , error => {
        new Error(error)
      })
  }

  ngOnInit(): void {
    document.body.className = "selector";
  }

  deleteProject(id: number) {

    this.authsurvice.deleteProject(id).subscribe(data => {
      console.log(data)
      window.location.reload();
    }, error => {
      new Error(error)
    }
    )
  }

  navigateToCreateProject() {
    this.router.navigate(['create-project'])
  }

  navigateToBacklog(projectId: number, projectName: String) {
    this.router.navigate(['backlog'], { queryParams: { projectId: projectId, projectName: projectName } })
  }
}


