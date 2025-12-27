import {TicketDto} from "./ticketDto";
import {EventDto} from "./eventDto";

export interface TicketDetailsDto {
  id?: number;
  price?: number;
  eventDto?: EventDto;
}
