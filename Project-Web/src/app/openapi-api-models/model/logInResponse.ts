import {EventDto} from "./eventDto";

export interface LogInResponse{
  id:number;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  role: string;
  dateOfBirth?: string;
  city?: string;
  county?: string;
  events?: Array<EventDto>;
}
