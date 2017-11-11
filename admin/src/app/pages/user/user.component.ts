import {Component, OnInit} from "@angular/core";
import {UserService} from "./user.service";
import {INglDatatableSort} from "ng-lightning/ng-lightning";

@Component({
    selector: 'app-user',
    templateUrl: './user.component.html',
    styleUrls: ['./user.component.css'],
    providers: [UserService]
})
export class UserComponent implements OnInit {

    data;
    page: number;
    total: number;

    sort: INglDatatableSort = {key: 'id', order: 'asc'};

    constructor(private userService: UserService) {
    }

    ngOnInit(): void {
        this.onPageChange();
    }

    onSort($event: INglDatatableSort) {
        const {key, order} = $event;
        this.data.sort((a: any, b: any) => {
            return (key === 'id' ? b[key] - a[key] : b[key].localeCompare(a[key])) * (order === 'desc' ? 1 : -1);
        });
    }

    onPageChange(pageNumber: number = 1) {
        if (pageNumber < 1)
            return;
        this.userService.getUsers(pageNumber, 10).subscribe(users => {
            this.data = users.content;
            this.page = users.number + 1;
            this.total = users.totalElements;
        })
    }

    onDelete(id: number) {
        this.userService.deleteUser(id).subscribe(response => {
            // if (response.result == 'success')
            //     this.onPageChange(this.page);
            // else
            //     console.log("error");
        });
    }
}
