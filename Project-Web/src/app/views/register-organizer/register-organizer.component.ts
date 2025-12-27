import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthControllerService, OrganizerDto, StandardUserDto} from "../../openapi-api-models";
import {NotificationService} from "../../shared/services/notification.service";
import {passwordValidator} from "../../shared/helpers/password-validator";

@Component({
  selector: 'app-register-organizer',
  templateUrl: './register-organizer.component.html',
  styleUrls: ['./register-organizer.component.css']
})
export class RegisterOrganizerComponent implements OnInit{

  signUpForm: FormGroup;

  constructor(private router: Router,
              private authService: AuthControllerService,
              private notification: NotificationService) {
  }

  ngOnInit() {
    this.signUpForm = new FormGroup({
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      email: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
      repeatPassword: new FormControl('', Validators.required),
    }, {validators: passwordValidator});
  }

  validatePassword() {
    var password = document.getElementById("signup-password-organizer") as HTMLInputElement;
    var confirm_password = document.getElementById(
      "signup-repeatPassword-organizer"
    ) as HTMLInputElement;

    if (password.value !== confirm_password.value) {
      confirm_password.setCustomValidity("Passwords Don't Match");
    } else {
      confirm_password.setCustomValidity("");
    }
  }

  onClick() {
    const signUpData = this.signUpForm.value;
    const organizer: OrganizerDto = {
      firstName: signUpData.firstName,
      lastName: signUpData.lastName,
      email: signUpData.email,
      password: signUpData.password
    }
    this.authService.registerOrganizer(organizer).subscribe(response => {
      this.notification.showPopupMessage("Register organizer successfully!", "OK");
      this.router.navigate(['/home']);
    }, error => {
      this.notification.showPopupMessage("Register organizer failed!", "OK");

    })
  }
}
