import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class IsLoggedUser implements CanActivate {

  constructor(private router: Router) {
  }

  canActivate(): boolean {
    const role = JSON.parse(localStorage.getItem('current_user') || '{}').role
    if (role==="USER") {
      return true; // user is not logged in, allow access
    } else {
      this.router.navigate(['/home']); // user is logged in, redirect to home page
      return false; // prevent access to the current route
    }
  }

}
