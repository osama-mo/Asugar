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

  userEmail: String | null = "";
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
    this.userEmail = this.route.snapshot.queryParamMap.get('userEmail');
    this.authsurvice.getMembersDetails(this.userEmail!).subscribe(
      data => {
        this.memberdetails = data
      }
      ,error => {
        new Error(error)
      })
  }

}
