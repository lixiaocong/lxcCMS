import {Component, OnInit} from "@angular/core";
import {User} from "./user-item/user-item.component";
import {UserService} from "./user.service";

@Component({
    selector: 'app-user',
    templateUrl: './user.component.html',
    styleUrls: ['./user.component.css'],
    providers: [UserService]
})
export class UserComponent implements OnInit {
    users: User[];

    constructor(private userService: UserService) {
    }

    ngOnInit() {
        this.users = [];
        this.userService.connect();
        this.userService.messages.subscribe((message: any) => {
            console.log(message);
        });

        this.userService.send("hello");
    }
}

class UserCommand {
    static GET_USERS: string = 'get-users';

    method: string;

    constructor(method: string) {
        this.method = method;
    }


}

class UserResult {
    method: string;
    data: any;
}