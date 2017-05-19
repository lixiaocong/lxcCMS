import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {User} from "./user-item/user-item.component";
import {PageDataHandler} from "../../utils/PageDataHandler";

@Injectable()
export class UserService {

    private userUrl:string = "http://localhost:8080/user";

    constructor(private http:Http) {
    }

    getUserNumber():Observable<number>{
        return this.http.get(this.userUrl)
            .map(PageDataHandler.extractData)
            .filter(PageDataHandler.successResponseFilter)
            .map(data=>data.users.totalElements)
    }

    getUsers(page:number = 1, size:number = 10):Observable<User[]>{
        return this.http.get(this.userUrl)
            .map(PageDataHandler.extractData)
            .filter(PageDataHandler.successResponseFilter)
            .map(data=>data.users.content)
    }
}
