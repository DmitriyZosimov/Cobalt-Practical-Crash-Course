import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {catchError, map, tap} from 'rxjs/operators';
import {environment} from "../../environments/environment";
import {FolderAndFileRequestModel} from "../model/folderAndFileRequestModel";
import {isFile} from "@angular/compiler-cli/src/ngtsc/file_system/testing/src/mock_file_system";

@Injectable({
  providedIn: 'root'
})
export class MainService {

  private basePath = environment.basePath;

  constructor(protected httpClient: HttpClient) {
  }

  public getFiles(url: string): Observable<HttpResponse<Array<string>>> {
    console.log('service URL: ' + `${this.basePath}${url}`);
    return this.httpClient.get<Array<string>>(`${this.basePath}${url}/`, {observe: 'response'})
      .pipe(
        map(response => response || []),
        tap(response => console.log(JSON.stringify(response))),
        catchError(this.handleError)
      );
  }

  public createFolderOrFile(url: string, model: FolderAndFileRequestModel): void {
    console.log('service Model name: ' + model.name);
    console.log('service Model isFile: ' + model.isFile);
    if(model.name === null || model.name === undefined) {
      throw new Error('The folder/file name is required');
    }
    if(model.isFile === null || model.isFile === undefined) {
      model.isFile = false;
    }
    console.log('service URL: ' + `${this.basePath}${url}`);
    this.httpClient.post(`${this.basePath}${url}`, null, {params: {name: model.name, file: model.isFile}})
      .pipe(
        map(response => response || []),
        tap(response => console.log(JSON.stringify(response))),
        catchError(this.handleError)
      ).subscribe();
  }

  private handleError(err: HttpErrorResponse): Observable<never> {
    let errorMessage: string;

    // A client-side or network error occurred.
    if (err.error instanceof Error) {
      errorMessage = `An error occurred: ${err.error.message}`;
    }
    // The backend returned an unsuccessful response code.
    // The response body may contain clues as to what went wrong,
    else {
      errorMessage = `Backend returned code ${err.status}, body was: ${err.error}`;
    }
    console.error(errorMessage);
    return Observable.throw(errorMessage);
  }
}
