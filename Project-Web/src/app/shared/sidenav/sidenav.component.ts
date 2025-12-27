import {AfterContentInit, Component, EventEmitter, OnInit, Output} from '@angular/core';
import {
  navbarData,
  navbarDataAdmin,
  navbarDataOrganizer,
  navbarDataOrganizerAndAdmin,
  navbarDataUser
} from "./nav-data";
import {Router} from "@angular/router";


@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent implements OnInit, AfterContentInit {

  collapsed = true;
  logged: boolean;
  init: boolean;
  screenWidth = 0;
  navData = navbarData;
  activeLink: string;

  constructor(private router: Router) {
    this.collapsed = true;
  }


  toggleCollapse() {
    this.collapsed = !this.collapsed;

  }

  closeSidenav() {
    this.collapsed = false;

  }

  ngOnInit(): void {
    this.collapsed = true;

    console.log("collapse")
    console.log(this.collapsed)

    const currentUser = localStorage.getItem('current_user');
    if (currentUser) {
      this.logged = true;

      const rol = JSON.parse(localStorage.getItem('current_user') || '{}').role;
      if (rol == "ADMIN") {
        this.navData = this.navData.concat(navbarDataOrganizerAndAdmin);
        this.navData = this.navData.concat(navbarDataAdmin);
      } else if (rol == "ORGANIZER") {
        this.navData = this.navData.concat(navbarDataOrganizer);
        this.navData = this.navData.concat(navbarDataOrganizerAndAdmin);
      } else if (rol == "USER") {
        this.navData = this.navData.concat(navbarDataUser);
      }
    } else {
      this.logged = false;
    }

    console.log(this.logged)

  }

  signOut() {
    localStorage.removeItem('current_user');
    this.logged = false;
    location.reload();

  }

  ngAfterContentInit(): void {
    this.init = true;
  }
}
