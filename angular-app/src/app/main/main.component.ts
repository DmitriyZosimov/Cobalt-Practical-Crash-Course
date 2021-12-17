import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {NgbToast} from "@ng-bootstrap/ng-bootstrap";
import {ToastService} from "../services/toast.service";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.sass']
})
export class MainComponent implements OnInit {

  constructor(private router: Router,
              private toastService: ToastService) { }

  ngOnInit(): void {
  }

  showFolderToast(): void {
    this.toastService.show('Create a new folder' , false, {
      classname: 'bg-light',
      autohide: false,
      headertext: 'Create a folder'
    });
  }

  showFileToast(): void {
    this.toastService.show('Create a new file' , true, {
      classname: 'bg-light',
      autohide: false,
      headertext: 'Create a file'
    });
  }

}
