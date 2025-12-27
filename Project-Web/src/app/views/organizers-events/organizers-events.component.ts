import {Component, OnInit} from '@angular/core';
import {EventControllerService, EventDto, LocationControllerService, LocationDto} from "../../openapi-api-models";
import {Router} from "@angular/router";
import {NotificationService} from "../../shared/services/notification.service";
import {MatDialog} from "@angular/material/dialog";
import {AddEventComponent} from "../add-event/add-event.component";
import {UpdateEventComponent} from "../../modals/update-event/update-event.component";

@Component({
  selector: 'app-organizers-events',
  templateUrl: './organizers-events.component.html',
  styleUrls: ['./organizers-events.component.css']
})
export class OrganizersEventsComponent implements OnInit {
  events: EventDto[] = []
  private retrieveResonse: any;
  private base64Data: any;

  constructor(private router: Router,
              private locationService: LocationControllerService,
              private eventService: EventControllerService,
              private notification: NotificationService,
              private dialogRef: MatDialog) {
  }

  ngOnInit(): void {
    const id = JSON.parse(localStorage.getItem('current_user') || '{}').id;

    this.eventService.getEventsByOrganizer(id).pipe().subscribe(result => {
      this.events = result;
      console.log(this.events)
      this.events.forEach(event => {
        this.retrieveResonse = event.image;
        this.base64Data = this.retrieveResonse.picByte;
        event.imageString = 'data:image/jpeg;base64,' + this.base64Data;
      })
    })
  }

  editEvent(event: EventDto, ev: Event) {
    ev.stopPropagation();

    const ref = this.dialogRef.open(UpdateEventComponent, {
      width: '820px',
      height: '700px',
      data: {
        "idEvent": event.id,
        "name": event.name,
        "category": event.category,
        "description": event.description,
        "county": event.location.county,
        "city": event.location.city,
        "location": event.location.id,
        "date": event.date,
        "time": event.hour,
        "ticketPrice": (!event.tickets || !event.tickets[0]) ? null : event.tickets[0].price,
        "ticketDiscount": (!event.tickets || !event.tickets[0]) ? null : event.tickets[0].discount,
        "availableTickets":event.availableTickets,
        "soldTickets":event.soldTickets,
        "file":event.image
      }
    })

    ref.afterClosed().subscribe(result => {
      if (result !== undefined && result !== null) {
        this.notification.showPopupMessage(result, "OK");
        setTimeout(() => {
          location.reload();
        }, 1500);
      }
    });


  }

  goToEventDetails(eventId: number | undefined) {
    this.router.navigate(['/event-details'], {queryParams: {id: eventId}});
  }

  deleteEvent(event: EventDto, ev: Event) {
    console.log(event)
    ev.stopPropagation();
    this.eventService.deleteEvent(event.id).subscribe(result => {
      this.notification.showPopupMessage("Event deleted successfully!", "OK");
      setTimeout(() => {
        location.reload();
      }, 1500);
    }, error => {
      this.notification.showPopupMessage("Event deletion failed!", "OK");
    })
  }
}
