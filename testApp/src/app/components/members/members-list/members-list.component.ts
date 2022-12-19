import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'app/components/auth/shared/auth.service';

@Component({
  selector: 'app-members-list',
  templateUrl: './members-list.component.html',
  styleUrls: ['./members-list.component.css']
})
export class MembersListComponent implements OnInit {
  projectId: String | null = "";
  projectName: String | null = "";

  members = []
  name = "Osama";
  surname = "Al moselli";
  constructor(private route: ActivatedRoute, private router: Router, private authsurvice: AuthService) {

  }

  ngOnInit(): void {
    document.body.className = "selector";
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');

    this.authsurvice.getMembersList(Number(this.projectId)).subscribe(
      data => {
        this.members = data
      }
      ,error => {
        new Error(error)
      })
  }

  navigateToAddmember() {
    this.router.navigate([''], { queryParams: { projectId: this.projectId,projectName:this.projectName} })
  }
  
  navigateToMemberDetails(){
    this.router.navigate(['member-details'], { queryParams: { projectId: this.projectId,projectName:this.projectName} })
  }
}
