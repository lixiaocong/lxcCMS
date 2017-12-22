import {Component, Input, OnInit} from "@angular/core";

@Component({
    selector: 'app-user-item',
    templateUrl: './user-item.component.html',
    styleUrls: ['./user-item.component.css']
})
export class UserItemComponent implements OnInit {
    @Input() user: User;

    constructor() {
    }

    ngOnInit() {
    }

}

export class User {
    id: number;
    createTime: number;
    lastUpdateTime: number;
    username: string;
}
