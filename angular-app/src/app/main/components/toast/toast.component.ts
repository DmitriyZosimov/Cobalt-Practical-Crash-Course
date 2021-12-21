import {Component, OnInit, TemplateRef} from '@angular/core';
import {ToastService} from "../../../services/toast.service";
import {MainService} from "../../../services/main.service";
import {FolderAndFileRequestModel} from "../../../model/folderAndFileRequestModel";
import {Router} from "@angular/router";
import {NgbToast} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-toast',
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.sass']
})
export class ToastComponent implements OnInit {

  model: FolderAndFileRequestModel;

  constructor(public toastService: ToastService,
              private mainService: MainService,
              private router: Router) {
    this.model = new FolderAndFileRequestModel();
  }

  ngOnInit(): void {
  }

  isTemplate(toast) { return toast.textOrTpl instanceof TemplateRef; }

  onClick(toast: NgbToast) {
    console.log('onSave ' + JSON.stringify(this.model));
    this.mainService.createFolderOrFile(this.router.url, this.model);
    this.toastService.remove(toast);
  }

  onFileSubmit(toast: NgbToast) {
    this.model.isFile = true;
    this.onClick(toast);
  }

  onFolderSubmit(toast: NgbToast) {
    this.model.isFile = false;
    this.onClick(toast);
  }
}
