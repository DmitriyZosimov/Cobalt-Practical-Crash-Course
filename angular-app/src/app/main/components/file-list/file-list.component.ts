import {Component, OnInit} from '@angular/core';
import {MainService} from '../../../services/main.service';
import {Router} from "@angular/router";

@Component({
  templateUrl: './file-list.component.html',
  styleUrls: ['./file-list.component.sass']
})
export class FileListComponent implements OnInit {

  files;
  url;
  header;

  constructor(
    private mainService: MainService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.mainService.getFiles(this.router.url)
      .subscribe(resp => {
        this.header = (resp.headers.get("File-Content") == "true");
        this.files = resp.body;
      });
    this.url = this.router.url;
  }

  onLink(url: any): void {
    this.url = this.router.url + '/' + url;
    this.mainService.getFiles(this.url).subscribe(resp => {
      this.header = (resp.headers.get("File-Content") == "true");
      this.files = resp.body;
    });
  }

  onCreateFolder(name: string): void {
    const link = ['/**'];
    this.router.navigate(link);
  }

  delete(file: any) {
    this.url = this.router.url + '/' + file;
    console.log("DELETE ----->> " + this.url);
    this.mainService.delete(this.url);
  }
}
