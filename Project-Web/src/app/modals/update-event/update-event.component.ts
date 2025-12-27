import {Component, Inject, OnInit} from '@angular/core';
import {Locations} from "../../shared/locations";
import {
  EventControllerService,
  EventDto,
  LocationControllerService,
  LocationDto,
  TicketDto
} from "../../openapi-api-models";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {DataService} from "../../shared/services/data.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NotificationService} from "../../shared/services/notification.service";
import {passwordValidator} from "../../shared/helpers/password-validator";

import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {empty} from "rxjs";
import {FileHandle} from "../../openapi-api-models/model/FileHandle";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-update-event',
  templateUrl: './update-event.component.html',
  styleUrls: ['./update-event.component.css']
})
export class UpdateEventComponent implements OnInit {
  counties = Locations.counties
  cities = Locations.cities
  locations: Array<LocationDto>

  selectedCategory: string = '';
  categoryInput: boolean = false;
  isSelectedCategory: boolean = true;
  selectedCounty: string = '';
  isCountySelected: boolean = true;

  createEventForm: FormGroup;
  selectedCity: string = '';
  isCitySelected: boolean = true;
  isLocationSelected: boolean = true;
  selectedLocation: any;

  current_user: any;

  categories: String[] = []
  locationInput: boolean;

  localUrl: any;
  selectedFile: FileHandle;
  oldSelectedFile: FileHandle;
  private availableTickets: any;
  private soldTickets: any;
  private organizerId: any;

  constructor(private data: DataService,
              private dialogRef: MatDialogRef<UpdateEventComponent>,
              private router: Router,
              private sanitizer: DomSanitizer,
              private activeRouter: ActivatedRoute,
              private locationService: LocationControllerService,
              private eventService: EventControllerService,
              private notification: NotificationService,
              @Inject(MAT_DIALOG_DATA) private formData: any) {
  }


  initialValues: null;

  ngOnInit() {
    this.current_user = JSON.parse(localStorage.getItem('current_user') || '{}')

    this.getCategories();

    this.createEventForm = new FormGroup({
      name: new FormControl(this.formData.name, Validators.required),
      category: new FormControl(this.formData.category, Validators.required),
      description: new FormControl(this.formData.description, Validators.required),
      county: new FormControl(this.formData.county, Validators.required),
      city: new FormControl(this.formData.city, Validators.required),
      location: new FormControl(this.formData.location, Validators.required),
      date: new FormControl(this.formData.date, Validators.required),
      time: new FormControl(this.formData.time, Validators.required),
      ticketPrice: new FormControl(this.formData.ticketPrice, Validators.required),
      ticketDiscount: new FormControl(this.formData.ticketDiscount, Validators.required),
      file: new FormControl(this.formData.file),
    }, {validators: passwordValidator});

    this.organizerId = this.formData.organizerId;
    this.oldSelectedFile = this.formData.file;

    this.selectedCategory = this.formData.category;
    this.selectedCounty = this.formData.county;
    this.selectedCity = this.formData.city;
    this.availableTickets = this.formData.availableTickets;
    this.soldTickets = this.formData.soldTickets;


    this.getLocations();

    this.selectedLocation = this.formData.location;
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

  onClick() {

    const creatEventData = this.createEventForm.value;

    const tickets: TicketDto[] = [];
    tickets.push({
      price: creatEventData.ticketPrice,
      discount: (creatEventData.ticketDiscount != undefined && !empty(creatEventData.ticketDiscount)) ? creatEventData.ticketDiscount : '0'
    })


    this.locationService.getLocationById(this.createEventForm.value.location).subscribe(response => {

      const event: EventDto = {
        name: creatEventData.name,
        description: creatEventData.description,
        category: creatEventData.category,
        location: response,
        date: creatEventData.date as string,
        hour: creatEventData.time as string,
        tickets: tickets,
        organizerId: this.organizerId,
        availableTickets: this.availableTickets,
        soldTickets: this.soldTickets,
        image: this.oldSelectedFile
      }

      if (this.selectedFile) {
        const formData = new FormData();
        formData.append('file', this.selectedFile.file, this.selectedFile.file.name);
        const blob: Blob = formData.get('file') as Blob;

        this.eventService.updateEventImage(this.formData.idEvent, event, blob).pipe().subscribe(response => {
          this.dialogRef.close("Event updated successfully");
        }, error => {
          this.dialogRef.close("Event updated successfully");
        })
      } else {
        this.eventService.updateEvent(this.formData.idEvent, event).pipe().subscribe(response => {
          this.dialogRef.close("Event updated successfully");
        }, error => {
          this.dialogRef.close("Event updated successfully");
        })
      }


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
