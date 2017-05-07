import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {User} from "./user-item/user-item.component";

@Injectable()
export class UserService {

    private userUrl:string = "http://localhost:8080/user";

    constructor(private http:Http) {
    }

    getUserNumber():Observable<number>{
        return this.http.get(this.userUrl)
            .map(this.extractData)
            .filter(this.successResponseFilter)
            .map(data=>data.users.totalElements)
    }

    getUsers(page:number = 1, size:number = 10):Observable<User[]>{
        return this.http.get(this.userUrl)
            .map(this.extractData)
            .filter(this.successResponseFilter)
            .map(data=>data.users.content)
    }

    private extractData(res:Response){
        return res.json();
    }
    private successResponseFilter(data){
        return data.result == 'success';
    }
}
