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

    ngOnInit(): void {
        this.userService.getUsers().subscribe((users: User[]) => {
            this.users = users;
        });
    }

}
