export * from './authController.service';
import { AuthControllerService } from './authController.service';
export * from './eventController.service';
import { EventControllerService } from './eventController.service';
export * from './locationController.service';
import { LocationControllerService } from './locationController.service';
export * from './userController.service';
import { UserControllerService } from './userController.service';
export const APIS = [AuthControllerService, EventControllerService, LocationControllerService, UserControllerService];
