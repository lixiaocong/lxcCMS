import {Injectable} from '@angular/core';
import {Http, URLSearchParams} from "@angular/http";
import {PageDataHandler} from "../../utils/PageDataHandler";
import {Observable} from "rxjs";

@Injectable()
export class FileService {

    private fileUrl: string = "http://localhost:8080/file/video";

    constructor(private http: Http) {
    }

    getFiles(): Observable<any> {
        return this.http.get(this.fileUrl)
            .map(PageDataHandler.extractData)
            .filter(PageDataHandler.successResponseFilter);
    }

    deleteFile(fileName: string) {
        let params = new URLSearchParams();
        params.set("fileName",fileName);
        return this.http.delete(this.fileUrl,{search:params})
            .map(PageDataHandler.extractData);
    }
}
