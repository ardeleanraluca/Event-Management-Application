import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {LocationControllerService, LocationDto, UserControllerService} from "../../openapi-api-models";
import {NotificationService} from "../../shared/services/notification.service";
import {OrganizerDetails} from "../../openapi-api-models/model/organizerDetails";

@Component({
  selector: 'app-organizers-table',
  templateUrl: './organizers-table.component.html',
  styleUrls: ['./organizers-table.component.css']
})
export class OrganizersTableComponent {
   organizers: Array<OrganizerDetails>;

  constructor(private router: Router,
              private userService: UserControllerService,
              private notification: NotificationService) {
  }

  ngOnInit(): void {
    this.userService.getAllOrganizers().pipe().subscribe(result => {
      this.organizers = result;
    })

  }


  deleteOrganizer(id: number) {
    this.userService.deleteOrganizer(id).pipe().subscribe(result => {
      this.notification.showPopupMessage("Organizer and his events deleted successfully!", "OK");
      setTimeout(() => {
        location.reload();
      }, 1500);
    }, error => {
      this.notification.showPopupMessage("Organizer deletion failed!", "OK");
    })

  }

}

