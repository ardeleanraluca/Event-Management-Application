import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {EventControllerService, EventDto, OrganizerDto, UserControllerService} from "../../openapi-api-models";
import {NotificationService} from "../../shared/services/notification.service";
import {UpdateEventComponent} from "../../modals/update-event/update-event.component";
import {MatDialog} from "@angular/material/dialog";
import {isEmpty} from "rxjs";
import {BoughtTicketDto} from "../../openapi-api-models/model/boughtTicketDto";
import {BoughtTicketControllerService} from "../../openapi-api-models/api/boughtTicketController.service";
import {isObjectEmpty} from "ngx-bootstrap/chronos/utils/type-checks";
import {OrganizerDetails} from "../../openapi-api-models/model/organizerDetails";

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.css']
})
export class EventDetailsComponent implements OnInit {
  event: EventDto | undefined
  isOrganizerOrAdmin: boolean;
  isAdmin: boolean
  noTickets: number;
  private retrieveResonse: any;
  private base64Data: any;
  organizer: OrganizerDetails | undefined;

  constructor(private router: Router,
              private dialogRef: MatDialog,
              private activeRouter: ActivatedRoute,
              private eventService: EventControllerService,
              private boughtTicketsService: BoughtTicketControllerService,
              private usersService: UserControllerService,
              private notification: NotificationService) {
  }

  ngOnInit(): void {
    const role = JSON.parse(localStorage.getItem('current_user') || '{}').role
    if (role === "ADMIN" || role === "ORGANIZER") {
      this.isOrganizerOrAdmin = true;
    } else {
      this.isOrganizerOrAdmin = false;
    }
    if (role === "ADMIN") {
      this.isAdmin = true;
    }


    this.activeRouter.queryParams.pipe().subscribe(params => {
        console.log(params['id']);
        this.eventService.getEventById(params['id']).pipe().subscribe(result => {
          this.event = result;
          this.retrieveResonse = this.event.image;
          this.base64Data = this.retrieveResonse.picByte;
          this.event.imageString = 'data:image/jpeg;base64,' + this.base64Data;

          if (this.isOrganizerOrAdmin) {

            const idorg: number = Number(this.event.organizerId);
            this.usersService.getOrgById(idorg).pipe().subscribe(res => {
              this.organizer = res;
              console.log(this.organizer)
            })

          }


          // @ts-ignore
          document.getElementById("id-description").innerHTML = '<p>' + this.event.description.replaceAll(/(\n+)/g, '</p><p>') + '</p>';


        });

      }
    );
    console.log("heteee")
    console.log(this.event)
  }

  editEvent(event: EventDto | undefined, ev: Event) {
    ev.stopPropagation();

    if (event !== undefined) {

      const ref = this.dialogRef.open(UpdateEventComponent, {
        width: '820px',
        height: '700px',
        data: {
          "organizerId": event.organizerId,
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
          "file": event.image
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

  }

  buyTickets() {

    if (!this.noTickets) {
      this.notification.showPopupMessage("Please enter a number of tickets that you want to buy", "OK");
      return;
    }

    const user = JSON.parse(localStorage.getItem('current_user') || '{}')
    const isEmpty = Object.keys(user).length === 0;

    if (!isEmpty) {
      const boughtTickets: Array<BoughtTicketDto> = [];
      const ticket: BoughtTicketDto = {
        typedTicketId: this.event?.tickets[0].id,
        userId: user.id
      }

      if (this.noTickets) {
        for (let i = 0; i < this.noTickets; i++) {
          boughtTickets.push(ticket);
        }

        this.boughtTicketsService.buyTickets(boughtTickets).pipe().subscribe(res => {
          this.notification.showPopupMessage("Tickets bought successfully", "OK");
          setTimeout(() => {
            location.reload();
          }, 1500);
        })

      }

    } else {
      this.notification.showPopupMessage("Login to buy tickets", "OK");
    }

  }

  deleteEvent() {
    if (this.event) {
      this.eventService.deleteEvent(this.event.id).subscribe(result => {
        this.notification.showPopupMessage("Event deleted successfully!", "OK");
        setTimeout(() => {
          this.router.navigate(['/home'])
        }, 1500);
      }, error => {
        this.notification.showPopupMessage("Event deletion failed!", "OK");
      })
    }
  }
}
