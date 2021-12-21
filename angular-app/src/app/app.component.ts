import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  title = 'angular-app';

  onActivate($event): void {
    console.log('Activated:', $event);
  }

  onDeactivate($event): void {
    console.log('Deactivated:', $event);
  }
}
