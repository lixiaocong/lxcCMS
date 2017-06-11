import {Response} from "@angular/http";
export class PageDataHandler {

    static extractData(res: Response) {
        return res.json();
    }

    static successResponseFilter(data) {
        return data.result == 'success';
    }
}