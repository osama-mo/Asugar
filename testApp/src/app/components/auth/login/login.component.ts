import { Component, OnInit } from '@angular/core';
import { AnimationOptions } from 'ngx-lottie';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { faKey } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  faUser = faUser;
  faKey = faKey;
  options: AnimationOptions = {
    path: 'https://assets1.lottiefiles.com/packages/lf20_mybx9f51.json', 
  };
  constructor() { }

  ngOnInit(): void {
  }
 

}
