import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MainRoutingModule } from './main-routing.module';
import { FileListComponent } from './components/file-list/file-list.component';
import { MainComponent } from './main.component';


@NgModule({
  declarations: [
    FileListComponent,
    MainComponent
  ],
  imports: [
    CommonModule,
    MainRoutingModule
  ]
})
export class MainModule { }
