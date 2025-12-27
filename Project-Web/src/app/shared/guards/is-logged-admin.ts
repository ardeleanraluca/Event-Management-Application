import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class IsLoggedAdmin implements CanActivate {

  constructor(private router: Router) {
  }

  canActivate(): boolean {
    const role = JSON.parse(localStorage.getItem('current_user') || '{}').role
    if (role==="ADMIN") {
      return true;
    } else {
      this.router.navigate(['/home']);
      return false;
    }
  }

}
