import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {PageDataHandler} from "../../utils/PageDataHandler";
import {environment} from "../../../environments/environment";

@Injectable()
export class ArticleService {

    private articleUrl: string = environment.articleUrl;

    constructor(private http: HttpClient) {
    }

    getArticles(page: number = 1, size: number = 10): Observable<any> {
        let params = new HttpParams().set("page", page.toString()).set("size", size.toString());
        return this.http.get(this.articleUrl, {params: params})
            .filter(PageDataHandler.successResponseFilter)
            .map(data => data.articles)
    }

    getArticleNumber(): Observable<number> {
        return this.getArticles().map(articles => articles.totalElements)
    }

    deleteArticle(id: number): Observable<any> {
        return this.http.delete(this.articleUrl + "/" + id)
    }
}
