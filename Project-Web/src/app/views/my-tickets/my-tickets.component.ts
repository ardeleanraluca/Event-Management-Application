import {Component, OnInit} from '@angular/core';
import {NgxQrcodeErrorCorrectionLevels} from "@techiediaries/ngx-qrcode";
import {BoughtTicketControllerService} from "../../openapi-api-models/api/boughtTicketController.service";
import {TicketDetailsDto} from "../../openapi-api-models/model/TicketDetailsDto";

@Component({
  selector: 'app-my-tickets',
  templateUrl: './my-tickets.component.html',
  styleUrls: ['./my-tickets.component.css']
})
export class MyTicketsComponent implements OnInit {
  ticketsDetails: Array<TicketDetailsDto> = []
  barcodeFormat = 'CODE128'; // The format of the barcode

  value = 'Techiediaries';
  protected readonly NgxQrcodeErrorCorrectionLevels = NgxQrcodeErrorCorrectionLevels;
  private retrieveResonse: any;
  private base64Data: any;

  constructor(private boughtTicketService: BoughtTicketControllerService) {
  }

  ngOnInit(): void {
    const id = JSON.parse(localStorage.getItem('current_user') || '{}');
    this.boughtTicketService.getTicketsByUserId(id).subscribe(result => {
      this.ticketsDetails = result;
      this.ticketsDetails.forEach(ticket => {
        this.retrieveResonse = ticket.eventDto?.image;
        this.base64Data = this.retrieveResonse.picByte;
        if (ticket.eventDto)
          ticket.eventDto.imageString = 'data:image/jpeg;base64,' + this.base64Data;
      })
    })


  }

  generateBarcodeValue(ticket: TicketDetailsDto): string {
    return btoa(ticket.id + "") + ticket.id;
  }


}
