import {Component, EventEmitter, HostListener, OnInit, Output} from '@angular/core';
import {Locations} from "../../shared/locations";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {NavigationEnd, Router} from "@angular/router";
import {
  AuthControllerService, EventControllerService,
  EventDto,
  LocationControllerService,
  LocationDto,
  TicketDto
} from "../../openapi-api-models";
import {NotificationService} from "../../shared/services/notification.service";
import {passwordValidator} from "../../shared/helpers/password-validator";
import {DataService} from "../../shared/services/data.service";
import {empty} from "rxjs";
import {FileHandle} from "../../openapi-api-models/model/FileHandle";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-add-event',
  templateUrl: './add-event.component.html',
  styleUrls: ['./add-event.component.css']
})
export class AddEventComponent implements OnInit {
  counties = Locations.counties
  cities = Locations.cities
  locations: Array<LocationDto>

  selectedCategory: string = '';
  categoryInput: boolean = false;
  isSelectedCategory: boolean = false;
  selectedCounty: string = '';
  isCountySelected: boolean;

  createEventForm: FormGroup;
  selectedCity: string = '';
  isCitySelected: boolean;
  isLocationSelected: boolean;
  selectedLocation: string = '';

  current_user: any;

  categories: String[] = []
  locationInput: boolean;

  selectedFile: FileHandle;

  constructor(private data: DataService,
              private router: Router,
              private sanitizer: DomSanitizer,
              private locationService: LocationControllerService,
              private eventService: EventControllerService,
              private notification: NotificationService) {
  }


  initialValues: any;

  ngOnInit() {
    this.current_user = JSON.parse(localStorage.getItem('current_user') || '{}')

    this.getCategories();

    this.createEventForm = new FormGroup({
      name: new FormControl('', Validators.required),
      category: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      county: new FormControl('', Validators.required),
      city: new FormControl('', Validators.required),
      location: new FormControl('', Validators.required),
      date: new FormControl('', Validators.required),
      time: new FormControl('', Validators.required),
      ticketPrice: new FormControl('', Validators.required),
      ticketDiscount: new FormControl('', Validators.required),
      file: new FormControl(''),
    }, {validators: passwordValidator});
    this.initialValues = this.createEventForm.value;
  }

  getCategories() {
    this.eventService.getCategories().pipe().subscribe(response => {
      this.categories = response;
    })
  }

  onCategorySelected() {
    if (this.selectedCategory.length != 0) {
      this.isSelectedCategory = true;
    } else {
      this.isSelectedCategory = false;
    }
    if (this.selectedCategory === 'Add a new one') {
      this.selectedCategory = '';
      this.categoryInput = true;
    }
  }

  onCountySelected() {
    this.locations = [];
    this.selectedLocation = '';
    if (this.selectedCounty.length != 0) {
      this.isCountySelected = true;
    } else {
      this.isCountySelected = false;
    }
  }

  onCitySelected() {
    this.locations = [];

    if (this.selectedCity.length != 0) {
      this.isCitySelected = true;
    } else {
      this.isCitySelected = false;
    }
  }

  onLocationSelected() {
    if (this.selectedLocation.length != 0) {
      this.isLocationSelected = true;
    } else {
      this.isLocationSelected = false;
    }
  }

  getLocations() {
    const county = this.createEventForm.value.county;
    const city = this.createEventForm.value.city;
    if (county !== "" && city !== "") {
      this.locationService.getLocationsByCountyAndCity(county, city).subscribe(response => {
        this.locations = response;
      })

    }
  }

  resetForm() {
    this.createEventForm.reset(this.initialValues);
    this.isCountySelected = false;
    this.isSelectedCategory = false;
    this.isLocationSelected = false;
    this.isCitySelected = false;
  }

  onClick() {

    const creatEventData = this.createEventForm.value;

    const tickets: TicketDto[] = [];
    if (creatEventData.ticketPrice && creatEventData.ticketPrice > 0) {
      tickets.push({
        price: creatEventData.ticketPrice,
        discount: (creatEventData.ticketDiscount != undefined && !empty(creatEventData.ticketDiscount)) ? creatEventData.ticketDiscount : '0'
      })
    }

    this.locationService.getLocationById(this.createEventForm.value.location).subscribe(response => {

      const event: EventDto = {
        name: creatEventData.name,
        description: creatEventData.description,
        category: creatEventData.category,
        location: response,
        date: creatEventData.date as string,
        hour: creatEventData.time as string,
        tickets: tickets,
        organizerId: this.current_user.id,
        image: this.selectedFile
      }

      console.log(this.current_user);
      console.log(event)


      const formData = new FormData();
      formData.append('file', event.image.file, event.image.file.name);
      const blob: Blob = formData.get('file') as Blob;

      this.eventService.createEvent(event,blob).pipe().subscribe(response => {
        this.notification.showPopupMessage("Event created successfully", "OK")
        this.resetForm();

      }, error => {
        this.notification.showPopupMessage("Create event failed", "OK")
      })

    })


  }

  addLocation() {
    this.selectedLocation = ''
    const queryParams = {
      county: this.selectedCounty,
      city: this.selectedCity
    }
    const url = this.router.createUrlTree(['/create-location'], {queryParams: queryParams}).toString();
    window.open(url, '_blank');
  }


  onFileSelected(event: any) {
    if (event.target.files) {
      const file = event.target.files[0];

      const fileHandle: FileHandle = {
        file: file,
        url: this.sanitizer.bypassSecurityTrustUrl(
          window.URL.createObjectURL(file)
        )
      }

      this.selectedFile = fileHandle;
      console.log(this.selectedFile)
    }
  }

}
