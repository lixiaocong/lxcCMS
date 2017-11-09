import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {PageDataHandler} from "../../utils/PageDataHandler";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";
import {LXCQueryEncoder} from "../../utils/CharEncoder";

@Injectable()
export class FileService {

    private fileUrl: string = environment.fileUrl;

    constructor(private http: HttpClient) {
    }

    getFiles(): Observable<any> {
        return this.http.get(this.fileUrl)
            .filter(PageDataHandler.successResponseFilter);
    }

    deleteFile(fileName: string) {
        let params = new HttpParams();
        params.set("fileName", fileName);
        return this.http.delete(this.fileUrl, {params: params})
    }
}
