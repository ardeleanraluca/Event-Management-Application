import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {RegisterUserComponent} from './views/register-user/register-user.component';
import {LoginPageComponent} from './views/login-page/login-page.component';
import {FirstPageComponent} from './views/first-page/first-page.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterModule, RouterOutlet, Routes} from "@angular/router";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from '@angular/common/http';
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {DataService} from "./shared/services/data.service";
import {NotificationService} from "./shared/services/notification.service";
import {MatInputModule} from "@angular/material/input";
import {RegisterOrganizerComponent} from './views/register-organizer/register-organizer.component';
import {MatSelectModule} from "@angular/material/select";
import {MatOption, MatOptionModule} from "@angular/material/core";
import {CdkDragPlaceholder} from "@angular/cdk/drag-drop";
import {CreateLocationComponent} from './views/create-location/create-location.component';
import {AddEventComponent} from './views/add-event/add-event.component';
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import {SidenavComponent} from './shared/sidenav/sidenav.component';
import {EventDetailsComponent} from './views/event-details/event-details.component';
import {NotLoggedInGuard} from "./shared/guards/not-logged-in-guard";
import {OrganizersEventsComponent} from './views/organizers-events/organizers-events.component';
import {MatDialogModule} from "@angular/material/dialog";
import {UpdateEventComponent} from './modals/update-event/update-event.component';
import {EventsLocationComponent} from './views/events-location/events-location.component';
import {MatTableModule} from "@angular/material/table";
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {IsLoggedAdmin} from "./shared/guards/is-logged-admin";
import {IsLoggedOrganizer} from "./shared/guards/is-logged-organizer";
import {MatGridListModule} from "@angular/material/grid-list";
import { MyTicketsComponent } from './views/my-tickets/my-tickets.component';
import {IsLogged} from "./shared/guards/is-logged";
import { NgxBarcode6Module } from 'ngx-barcode6';
import { NgxQRCodeModule } from '@techiediaries/ngx-qrcode';
import {IsLoggedUser} from "./shared/guards/is-logged-user";
import {IsLoggedAdminOrOrganizer} from "./shared/guards/is-logged-admin-or-organizer";
import { OrganizersTableComponent } from './views/organizers-table/organizers-table.component';

const routes: Routes = [
  {path: '', component: FirstPageComponent},
  {path: 'login', component: LoginPageComponent, canActivate: [NotLoggedInGuard]},
  {path: 'sign-up', component: RegisterUserComponent},
  {path: 'register-organizer', component: RegisterOrganizerComponent, canActivate: [IsLoggedAdmin]},
  {path: 'create-location', component: CreateLocationComponent, canActivate: [IsLoggedAdminOrOrganizer]},
  {path: 'add-event', component: AddEventComponent, canActivate: [IsLoggedOrganizer]},
  {path: 'home', component: FirstPageComponent},
  {path: 'event-details', component: EventDetailsComponent},
  {path: 'my-organized-events', component: OrganizersEventsComponent, canActivate: [IsLoggedOrganizer]},
  {path: 'locations', component: EventsLocationComponent, canActivate: [IsLoggedAdminOrOrganizer]},
  {path: 'my-tickets', component: MyTicketsComponent, canActivate: [IsLoggedUser]},
  {path: 'organizers', component: OrganizersTableComponent, canActivate: [IsLoggedAdmin]},
]

@NgModule({
  declarations: [
    AppComponent,
    RegisterUserComponent,
    LoginPageComponent,
    FirstPageComponent,
    RegisterOrganizerComponent,
    CreateLocationComponent,
    AddEventComponent,
    SidenavComponent,
    EventDetailsComponent,
    OrganizersEventsComponent,
    UpdateEventComponent,
    EventsLocationComponent,
    MyTicketsComponent,
    OrganizersTableComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(routes),
    ReactiveFormsModule,
    MatSnackBarModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatSelectModule,
    MatOptionModule,
    CdkDragPlaceholder,
    MatCardModule,
    MatButtonModule,
    MatToolbarModule,
    MatIconModule,
    MatSidenavModule,
    MatListModule,
    MatDialogModule,
    MatTableModule,
    NgxDatatableModule,
    MatGridListModule,
    NgxBarcode6Module,
    NgxQRCodeModule
  ],
  providers: [NotificationService, DataService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
