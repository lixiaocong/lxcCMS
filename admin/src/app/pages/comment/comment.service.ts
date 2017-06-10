import { Injectable } from '@angular/core';
import {Http, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs";
import {PageDataHandler} from "../../utils/PageDataHandler";

@Injectable()
export class CommentService {
    private commentUrl:string = "http://localhost:8080/comment";

    constructor(private http:Http) {
    }

    getCommends(page: number =1, size: number = 10) :Observable<any>{
        let params = new URLSearchParams();
        params.set("page",page.toString());
        params.set("size",size.toString());
        return this.http.get(this.commentUrl,{search:params})
            .map(PageDataHandler.extractData)
            .filter(PageDataHandler.successResponseFilter)
            .map(data=>data.comments)
    }

    getCommentNumber():Observable<number>{
        return this.getCommends().map(comments=>comments.totalElements);
    }

    deleteComment(id: number) {
        return this.http.delete(this.commentUrl+"/"+id)
            .map(PageDataHandler.extractData);
    }
}
