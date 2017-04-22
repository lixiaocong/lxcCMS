import {Component, OnInit} from "@angular/core";
import {DashboardItem} from "./dashboard-card/dashboard-card.component";

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
    ws: WebSocket;

    userCardItem: DashboardItem;
    articleCardItem: DashboardItem;
    commentCardItem: DashboardItem;

    constructor() {
    }

    ngOnInit() {
        this.userCardItem = new DashboardItem("user", "-");
        this.articleCardItem = new DashboardItem("article", "-");
        this.commentCardItem = new DashboardItem("comment", "-");

        let url: string = 'ws://localhost:8080/dashboard-socket';
        this.ws = new WebSocket(url);

        this.ws.onopen = event => {
            this.ws.send(JSON.stringify(new DashboardCommand(DashboardCommand.GET_USER_NUMBER)));
            this.ws.send(JSON.stringify(new DashboardCommand(DashboardCommand.GET_ARTICLE_NUMBER)));
            this.ws.send(JSON.stringify(new DashboardCommand(DashboardCommand.GET_COMMENT_NUMBER)));
        };

        this.ws.onclose = event => {
            console.log('close');
            console.log(event.code)
        };

        this.ws.onerror = event => {
            console.log('error');
        };

        this.ws.onmessage = event => {
            let result:DashboardResult = JSON.parse(event.data);
            switch (result.method){
                case DashboardCommand.GET_USER_NUMBER:this.userCardItem.content=result.data.toLocaleString();break;
                case DashboardCommand.GET_ARTICLE_NUMBER:this.articleCardItem.content=result.data.toLocaleString();break;
                case DashboardCommand.GET_COMMENT_NUMBER:this.commentCardItem.content=result.data.toLocaleString();break;
                default:break;
            }
        };
    }
}

class DashboardCommand{
    static GET_USER_NUMBER = "get-user-number";
    static GET_ARTICLE_NUMBER= "get-article-number";
    static GET_COMMENT_NUMBER = "get-comment-number";

    method:string;

    constructor(method:string){
        this.method = method;
    }
}

class DashboardResult{
    method:string;
    data:any;
}


