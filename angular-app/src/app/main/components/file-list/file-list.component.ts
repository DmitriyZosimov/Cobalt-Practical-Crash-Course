import { Component, OnInit } from '@angular/core';
import {MainService} from '../../../services/main.service';
import {Router} from "@angular/router";
import {HttpClient, HttpRequest} from "@angular/common/http";

@Component({
  templateUrl: './file-list.component.html',
  styleUrls: ['./file-list.component.sass']
})
export class FileListComponent implements OnInit {

  files;
  url;

  constructor(
    private mainService: MainService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.files = this.mainService.getFiles(this.router.url);
    this.url = this.router.url;
    console.log('ngOnInit router URL: ' + this.router.url);
    console.log('ngOnInit URL: ' + this.url);
  }

  onLink(url: any): void {
    console.log('onLink URL: ' + this.url);
    console.log('onLink router URL: ' + this.router.url);
    console.log('onLink param URL: ' + url);
    this.url = this.router.url + '/' + url;
    this.files = this.mainService.getFiles(this.url);

    console.log('onLink end router URL: ' + this.router.url);
    console.log('onLink end URL: ' + this.url);
  }

  onCreateFolder(name: string): void {
    const link = ['/**'];
    this.router.navigate(link);
  }

}
