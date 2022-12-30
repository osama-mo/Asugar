import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'app/components/auth/shared/auth.service';
import { MemberDetailsResponse } from './member-details-response';

@Component({
  selector: 'app-member-details',
  templateUrl: './member-details.component.html',
  styleUrls: ['./member-details.component.css']
})
export class MemberDetailsComponent implements OnInit {

  projectId: String | null = "";
  projectName: String | null = "";

  memberdetails: MemberDetailsResponse;


  constructor(private route: ActivatedRoute, private router: Router, private authsurvice: AuthService) {
    document.body.className = "selector";
    this.memberdetails = {
      firstName: "",
      Email: "",
      LastName: "",
      title: ""
    }
    
  }


  ngOnInit(): void {
    this.memberdetails.Email = this.route.snapshot.queryParamMap.get('email');
    this.projectId = this.route.snapshot.queryParamMap.get('projectId');
    this.projectName = this.route.snapshot.queryParamMap.get('projectName');
    this.memberdetails.firstName = this.route.snapshot.queryParamMap.get('fn');
    this.memberdetails.LastName= this.route.snapshot.queryParamMap.get('ln');
    this.memberdetails.title = this.route.snapshot.queryParamMap.get('title');
  }

}