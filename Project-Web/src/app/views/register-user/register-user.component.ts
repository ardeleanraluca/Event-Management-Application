import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {passwordValidator} from "../../shared/helpers/password-validator";
import {AuthControllerService, StandardUserDto} from "../../openapi-api-models";
import {Router} from "@angular/router";
import {DataService} from "../../shared/services/data.service";
import {NotificationService} from "../../shared/services/notification.service";
import {Locations} from "../../shared/locations";


@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.css']
})
export class RegisterUserComponent implements OnInit {
  counties: String[] = Locations.counties

  cities: { [key: string]: string[] } = Locations.cities


  signUpForm: FormGroup;
  selectedCounty: string;

  isCountySelected: boolean = false;
  isCitySelected: boolean = false;
  cityInput: boolean = false;
  selectedCity: string = '';


  constructor(private router: Router,
              private authService: AuthControllerService,
              private notification: NotificationService) {
  }


  ngOnInit() {
    this.signUpForm = new FormGroup({
      firstName: new FormControl('', Validators.required),
      lastName: new FormControl('', Validators.required),
      county: new FormControl('', Validators.required),
      city: new FormControl('', Validators.required),
      email: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required),
      repeatPassword: new FormControl('', Validators.required),
    }, {validators: passwordValidator});
  }

  validatePassword() {
    var password = document.getElementById("signup-password") as HTMLInputElement;
    var confirm_password = document.getElementById(
      "signup-repeatPassword"
    ) as HTMLInputElement;

    if (password.value !== confirm_password.value) {
      confirm_password.setCustomValidity("Passwords Don't Match");
    } else {
      confirm_password.setCustomValidity("");
    }
  }

  onCountySelected(): void {
    this.selectedCity = ' '
    this.isCountySelected = true;
    this.cityInput = false;
  }

  onCitySelected(): void {
    this.isCitySelected = true;
    if (this.selectedCity === 'Add a new one') {
      this.selectedCity = ''
      this.cityInput = true;
    }

  }

  onClick() {

    const signUpData = this.signUpForm.value;
    const user: StandardUserDto = {
      firstName: signUpData.firstName,
      lastName: signUpData.lastName,
      email: signUpData.email,
      password: signUpData.password,
      city: signUpData.city,
      county: signUpData.county
    }
    this.authService.register(user).subscribe(response => {
      this.notification.showPopupMessage("Register successfully!", "OK");
      this.router.navigate(['/login']);
    }, error => {
      this.notification.showPopupMessage("Register failed!", "OK");

    })

  }
}
