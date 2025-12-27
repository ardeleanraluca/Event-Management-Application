import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class IsLoggedOrganizer implements CanActivate {

  constructor(private router: Router) {
  }

  canActivate(): boolean {
    const role = JSON.parse(localStorage.getItem('current_user') || '{}').role
    if (role==="ORGANIZER") {
      return true; // user is not logged in, allow access
    } else {
      this.router.navigate(['/home']); // user is logged in, redirect to home page
      return false; // prevent access to the current route
    }
  }

}
