import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {PageDataHandler} from "../../utils/PageDataHandler";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";

@Injectable()
export class FileService {

    private fileUrl: string = environment.fileUrl;

    constructor(private http: HttpClient) {
    }

    getFiles(): Observable<any> {
        return this.http.get(this.fileUrl)
            .filter(PageDataHandler.successResponseFilter);
    }

    deleteFile(fileName: string): Observable<any> {
        return this.http.post(this.fileUrl + '/delete', fileName);
    }
}
