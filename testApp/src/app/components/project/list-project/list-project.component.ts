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

  projects = []

  constructor(private router: Router, private authsurvice: AuthService) {

    authsurvice.getProjectlist().subscribe(data => {
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

    this.authsurvice.deleteProject(id).subscribe(data => { })
    window.location.reload();

  }

  navigateToCreateProject() {
    this.router.navigate(['create-project'])
  }

  navigateToBacklog(projectId: String, projectName: String) {
    this.router.navigate(['backlog'], { queryParams: { projectId: projectId, projectName: projectName } })
  }
}


