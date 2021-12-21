import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MainComponent} from "./main.component";
import {FileListComponent} from "./components/file-list/file-list.component";

const routes: Routes = [
  {
    path: '**',
    component: MainComponent,
    children: [
      {
        path: '**',
        component: FileListComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainRoutingModule { }
