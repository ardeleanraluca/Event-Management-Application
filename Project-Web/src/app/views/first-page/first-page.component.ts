import {Component, OnInit} from '@angular/core';
import {range} from "rxjs";
import {AuthControllerService, EventControllerService, EventDto} from "../../openapi-api-models";
import {Router} from "@angular/router";
import {NotificationService} from "../../shared/services/notification.service";
import {DataService} from "../../shared/services/data.service";
import {Locations} from "../../shared/locations";
import {getTime} from "ngx-bootstrap/chronos/utils/date-getters";
import {formatDate} from "@angular/common";

@Component({
  selector: 'app-first-page',
  templateUrl: './first-page.component.html',
  styleUrls: ['./first-page.component.css']
})
export class FirstPageComponent implements OnInit {
  events: EventDto[] = [];
  eventsCopy: EventDto[] = [];
  private retrieveResonse: any;
  private base64Data: any;
  category: string = '';

  categories: String[] = []
  isSelectedCategory: boolean;

  counties = Locations.counties
  cities = Locations.cities
  county: string = '';
  city: string = '';
  sortDate: string = '';

  constructor(private router: Router,
              private eventService: EventControllerService,
              private notification: NotificationService) {
  }

  ngOnInit(): void {

    this.getCategories();

    this.eventService.getEvents().pipe().subscribe(result => {
      this.events = result;
      this.events.forEach(event => {
        this.retrieveResonse = event.image;
        this.base64Data = this.retrieveResonse.picByte;
        event.imageString = 'data:image/jpeg;base64,' + this.base64Data;
      })

      this.eventsCopy = this.events;
    })

  }

  getCategories() {
    this.eventService.getCategories().pipe().subscribe(response => {
      this.categories = response;
    })
  }

  goToEventDetails(eventId: number | undefined) {
// Navigate to the event details page and pass the event ID as a query parameter
    this.router.navigate(['/event-details'], {queryParams: {id: eventId}});
  }

  onSort() {
    console.log(this.sortDate)
    let sortedItems = [...this.eventsCopy]; // Create a copy of the original array

    if (this.category) {
      sortedItems = sortedItems.filter((item) => item.category === this.category);
    }

    if (this.county) {
      sortedItems = sortedItems.filter((item) => item.location.county === this.county);
    }

    if (this.city) {
      sortedItems = sortedItems.filter((item) => item.location.city === this.city);
    }

    sortedItems.sort((a, b) => {
      let d1 = new Date(a.date);
      let d2 = new Date(b.date);
      if (this.sortDate === "Asc") {
        return d1.getTime() - d2.getTime();
      } else if (this.sortDate === "Desc") {
        return d2.getTime() - d1.getTime();
      }
      return 0; // No specific sort order selected
    });

    this.events = sortedItems;

  }
}
