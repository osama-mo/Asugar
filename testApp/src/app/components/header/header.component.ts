import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth/shared/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  isloggedin :boolean = false

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {

    this.isloggedin = this.authService.isLoggedIn()
    if(this.isloggedin == false){
      this.router.navigate([''])
    }
  }

  logout(){
    this.authService.logout()
    this.router.navigate([''])
    this.isloggedin = false
  }
}
