import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {PageDataHandler} from "../../utils/PageDataHandler";
import {environment} from "../../../environments/environment";
import {HttpClient, HttpParams} from "@angular/common/http";

@Injectable()
export class UserService {

    private userUrl: string = environment.userUrl;

    constructor(private http: HttpClient) {
    }

    getUsers(page: number = 1, size: number = 10): Observable<any> {
        let params = new HttpParams().set("page", page.toString()).set("size", size.toString());
        return this.http.get(this.userUrl, {params: params})
            .filter(PageDataHandler.successResponseFilter)
            .map(data => data.users)
    }

    getUserNumber(): Observable<number> {
        return this.getUsers().map(users => users.totalElements);
    }

    deleteUser(id: number) {
        return this.http.delete(this.userUrl + "/" + id)
    }
}
