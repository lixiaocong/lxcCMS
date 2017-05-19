import { Injectable } from '@angular/core';
import {Http, Response} from "@angular/http";
import {Observable} from "rxjs";
import {PageDataHandler} from "../../utils/PageDataHandler";

@Injectable()
export class ArticleService {

    private articleUrl:string = "http://localhost:8080/article";

    constructor(private http:Http) {
    }

    getArticleNumber():Observable<number>{
        return this.http.get(this.articleUrl)
            .map(PageDataHandler.extractData)
            .filter(PageDataHandler.successResponseFilter)
            .map(data=>data.articles.totalElements)
    }
}
