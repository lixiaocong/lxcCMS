import {Component, OnInit} from "@angular/core";
import {DashboardItem} from "./dashboard-card/dashboard-card.component";
import {UserService} from "../user/user.service";

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css'],
    providers: [UserService]
})
export class DashboardComponent implements OnInit {
    userCardItem: DashboardItem;
    articleCardItem: DashboardItem;
    commentCardItem: DashboardItem;

    constructor(private userService: UserService) {
    }

    ngOnInit() {
        this.userCardItem = new DashboardItem("user", "-");
        this.articleCardItem = new DashboardItem("article", "-");
        this.commentCardItem = new DashboardItem("comment", "-");

        this.userService.getUserNumber().subscribe((userNumber: number) => {
            this.userCardItem.content = userNumber.toString();
        })
    }
}
