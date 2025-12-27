import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {
  AuthControllerService,
  OrganizerDto,
  StandardUserDto,
  UserLoginDto
} from "../../openapi-api-models";
import {NotificationService} from "../../shared/services/notification.service";
import {DataService} from "../../shared/services/data.service";
import {UserAccountDto} from "../../openapi-api-models";


@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {

  logInForm: FormGroup;

  constructor(private router: Router,
              private authService: AuthControllerService,
              private notification: NotificationService) {
  }

  ngOnInit() {

    this.logInForm = new FormGroup({
      email: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),

    });
  }

  onClick() {
    const logInData = this.logInForm.value;
    const user: UserLoginDto = {
      email: logInData.email,
      password: logInData.password
    }

    this.authService.login(user).pipe().subscribe(response => {
      let account = response as UserAccountDto;
      let current_user;
      if (account.role == "USER") {
        current_user = response as StandardUserDto
      } else if (account.role == "ORGANIZER") {
        current_user = response as OrganizerDto;
      } else {
        current_user = account;
      }

      localStorage.setItem('current_user', JSON.stringify(current_user));
      // console.log(JSON.parse(localStorage.getItem('current_user') || '{}'));

      // this.router.navigate(['/home']);
      this.router.navigate(['/home']);
    }, error => {
      this.notification.showPopupMessage("Username or password incorrect", "OK")
    })
  }
}
