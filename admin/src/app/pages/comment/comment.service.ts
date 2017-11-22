import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {PageDataHandler} from "../../utils/PageDataHandler";
import {environment} from "../../../environments/environment";

@Injectable()
export class CommentService {
    private commentUrl: string = environment.commentUrl;

    constructor(private http: HttpClient) {
    }

    getCommends(page: number = 1, size: number = 10): Observable<any> {
        let params = new HttpParams().set("page", page.toString()).set("size", size.toString());
        return this.http.get(this.commentUrl, {params: params})
            .filter(PageDataHandler.successResponseFilter)
            .map(data => data.comments)
    }

    getCommentNumber(): Observable<number> {
        return this.getCommends().map(comments => comments.totalElements);
    }

    deleteComment(id: number): Observable<any> {
        return this.http.delete(this.commentUrl + "/" + id)
    }
}
