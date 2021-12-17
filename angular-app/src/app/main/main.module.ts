import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MainRoutingModule } from './main-routing.module';
import { FileListComponent } from './components/file-list/file-list.component';
import { MainComponent } from './main.component';
import {NgbToastModule} from "@ng-bootstrap/ng-bootstrap";
import {ToastService} from "../services/toast.service";
import { ToastComponent } from './components/toast/toast.component';
import {FormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    FileListComponent,
    MainComponent,
    ToastComponent
  ],
  imports: [
    CommonModule,
    MainRoutingModule,
    NgbToastModule,
    FormsModule
  ],
  providers: [
    ToastService
  ]
})
export class MainModule { }
