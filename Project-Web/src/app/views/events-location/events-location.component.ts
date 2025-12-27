import {Component, OnInit} from '@angular/core';
import {EventControllerService, LocationControllerService, LocationDto} from "../../openapi-api-models";
import {DataService} from "../../shared/services/data.service";
import {Router} from "@angular/router";
import {NotificationService} from "../../shared/services/notification.service";
import {MatDatepicker} from "@angular/material/datepicker";
import {MatDialog} from "@angular/material/dialog";
import {UpdateEventComponent} from "../../modals/update-event/update-event.component";
import {CreateLocationComponent} from "../create-location/create-location.component";

@Component({
  selector: 'app-events-location',
  templateUrl: './events-location.component.html',
  styleUrls: ['./events-location.component.css']
})
export class EventsLocationComponent implements OnInit {
  locations: Array<LocationDto>

  constructor(private router: Router,
              private dialogRef: MatDialog,
              private locationService: LocationControllerService,
              private notification: NotificationService) {
  }

  ngOnInit(): void {
    this.locationService.getAllLocations().pipe().subscribe(result => {
      this.locations = result;
    })

  }


  deleteLocation(id:number) {
    this.locationService.deleteLocation(id).pipe().subscribe(result=>{
      this.notification.showPopupMessage("Location deleted successfully!","OK");
      setTimeout(() => {
        location.reload();
      }, 1500);
    },error => {
      this.notification.showPopupMessage("Location deletion failed - Location do not exists or exists events that take place here!","OK");
    })

  }

  addLocation() {
    this.router.navigate(['create-location'])
  }
}

