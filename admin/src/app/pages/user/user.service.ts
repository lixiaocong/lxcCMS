import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {PageDataHandler} from "../../utils/PageDataHandler";
import {Http, URLSearchParams} from "@angular/http";
import {environment} from "../../../environments/environment";

@Injectable()
export class UserService {

    private userUrl: string = environment.userUrl;

    constructor(private http: Http) {
    }

    getUserNumber(): Observable<number> {
        return this.getUsers().map(users => users.totalElements);
    }

    deleteUser(id: number) {
        return this.http.delete(this.userUrl + "/" + id)
            .map(PageDataHandler.extractData);
    }

    getUsers(page: number = 1, size: number = 10): Observable<any> {
        let params = new URLSearchParams();
        params.set("page", page.toString());
        params.set("size", size.toString());
        return this.http.get(this.userUrl, {search: params})
            .map(PageDataHandler.extractData)
            .filter(PageDataHandler.successResponseFilter)
            .map(data => data.users)
    }
}
