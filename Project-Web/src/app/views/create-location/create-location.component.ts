import {Component, OnInit} from '@angular/core';
import {Locations} from "../../shared/locations";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {LocationControllerService, LocationDto} from "../../openapi-api-models";
import {NotificationService} from "../../shared/services/notification.service";
import {DataService} from "../../shared/services/data.service";

@Component({
  selector: 'app-create-location',
  templateUrl: './create-location.component.html',
  styleUrls: ['./create-location.component.css']
})
export class CreateLocationComponent implements OnInit {


  counties = Locations.counties;
  cities = Locations.cities;
  createLocationForm: FormGroup;
  selectedCounty: string;
  isCitySelected: boolean;
  isCountySelected: boolean;
  haveQueryParam = false;
  selectedCity: string;
  initialForm: any;

  constructor(private data: DataService,
              private router: Router,
              private activeRouter: ActivatedRoute,
              private locationService: LocationControllerService,
              private notification: NotificationService) {
  }


  ngOnInit() {
    this.createLocationForm = new FormGroup({
      name: new FormControl('', Validators.required),
      address: new FormControl('', Validators.required),
      county: new FormControl('', Validators.required),
      city: new FormControl('', Validators.required),
      noSeats: new FormControl('', Validators.required),
    });
    this.initialForm = this.createLocationForm.value;

    this.activeRouter.queryParams.subscribe(params => {
        if (params.hasOwnProperty('county') && params.hasOwnProperty('city')) {
          this.haveQueryParam = true;
          this.selectedCounty = params['county'];
          this.selectedCity = params['city']
        }
      }
    );

    console.log(this.haveQueryParam)
  }

  onCountySelected():void {

    if (this.selectedCounty.length != 0) {
      this.isCountySelected = true;
    } else {
      this.isCountySelected = false;
    }
  }

  onCitySelected(): void {
    if (this.selectedCity.length != 0) {
      this.isCitySelected = true;
    } else {
      this.isCitySelected = false;
    }

  }

  onClick() {

    const createLocationData = this.createLocationForm.value;
    const location: LocationDto = {
      noSeats: createLocationData.noSeats,
      address: createLocationData.address,
      name: createLocationData.name,
      county: createLocationData.county,
      city: createLocationData.city
    }
    this.locationService.createLocation(location).subscribe(response => {
      this.notification.showPopupMessage("New location was created successfully!", "OK");
      this.createLocationForm.reset(this.initialForm);

    }, error => {
      this.notification.showPopupMessage("An address with this name already exists", "OK");

    })


  }
}
