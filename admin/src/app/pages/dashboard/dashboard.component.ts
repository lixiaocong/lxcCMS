import {Component, OnInit} from "@angular/core";
import {DashboardItem} from "./dashboard-card/dashboard-card.component";
import {UserService} from "../user/user.service";
import {ArticleService} from "../article/article.service";
import {CommentService} from "../comment/comment.service";

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css'],
    providers: [UserService, ArticleService, CommentService]
})
export class DashboardComponent implements OnInit {
    userCardItem: DashboardItem;
    articleCardItem: DashboardItem;
    commentCardItem: DashboardItem;

    constructor(private userService: UserService, private articleService: ArticleService, private commentService: CommentService) {
    }

    ngOnInit() {
        this.userCardItem = new DashboardItem("user", "-");
        this.articleCardItem = new DashboardItem("article", "-");
        this.commentCardItem = new DashboardItem("comment", "-");

        this.userService.getUserNumber().subscribe((userNumber: number) => {
            this.userCardItem.content = userNumber.toString();
        });

        this.articleService.getArticleNumber().subscribe((articleNumber: number) => {
            this.articleCardItem.content = articleNumber.toString();
        });

        this.commentService.getCommentNumber().subscribe((commentNumber: number) => {
            this.commentCardItem.content = commentNumber.toString();
        })
    }
}
