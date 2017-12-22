import {Component, OnInit} from "@angular/core";
import {UserService} from "./user.service";
import {MatTableDataSource, PageEvent} from '@angular/material';

@Component({
    selector: 'app-user',
    templateUrl: './user.component.html',
    styleUrls: ['./user.component.css'],
    providers: [UserService]
})
export class UserComponent implements OnInit {

    displayedColumns = ['id', 'username', 'action'];
    data = new MatTableDataSource<User>();
    page: number;
    total: number;

    constructor(private userService: UserService) {
    }

    ngOnInit(): void {
        this.onPageChange();
    }

    onDelete(id: number) {
        this.userService.deleteUser(id).subscribe(response => {
            if (response.result == 'success')
                this.onPageChange();
            else
                console.log("error");
        });
    }

    onPage(page: PageEvent) {
        this.onPageChange(page.pageIndex)
    }

    onPageChange(pageNumber: number = 1) {
        if (pageNumber < 1)
            return;
        this.userService.getUsers(pageNumber, 10).subscribe(users => {
            this.data.data = users.content;
            this.page = users.number + 1;
            this.total = users.totalElements;
        })
    }
}

export interface User {
    id: number;
    username: string
}
