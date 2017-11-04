import {Injectable} from "@angular/core";
import {Http, URLSearchParams} from "@angular/http";
import {PageDataHandler} from "../../utils/PageDataHandler";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";
import {LXCQueryEncoder} from "../../utils/CharEncoder";

@Injectable()
export class FileService {

    private fileUrl: string = environment.fileUrl;

    constructor(private http: Http) {
    }

    getFiles(): Observable<any> {
        return this.http.get(this.fileUrl)
            .map(PageDataHandler.extractData)
            .filter(PageDataHandler.successResponseFilter);
    }

    deleteFile(fileName: string) {
        let params = new URLSearchParams('',new LXCQueryEncoder());
        params.set("fileName", fileName);
        return this.http.delete(this.fileUrl, {search: params})
            .map(PageDataHandler.extractData);
    }
}
