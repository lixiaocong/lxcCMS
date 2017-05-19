import { Injectable } from '@angular/core';
import {Http} from "@angular/http";
import {Observable} from "rxjs";
import {PageDataHandler} from "../../utils/PageDataHandler";

@Injectable()
export class CommentService {
    private articleUrl:string = "http://localhost:8080/comment";

    constructor(private http:Http) {
    }

    getCommentNumber():Observable<number>{
        return this.http.get(this.articleUrl)
            .map(PageDataHandler.extractData)
            .filter(PageDataHandler.successResponseFilter)
            .map(data=>data.comments.totalElements)
    }
}
