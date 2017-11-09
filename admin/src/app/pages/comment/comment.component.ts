import {Component, OnInit} from "@angular/core";
import {INglDatatableRowClick, INglDatatableSort} from "ng-lightning/ng-lightning";
import {CommentService} from "./comment.service";

@Component({
    selector: 'app-comment',
    templateUrl: './comment.component.html',
    styleUrls: ['./comment.component.css'],
    providers: [CommentService]
})
export class CommentComponent implements OnInit {
    data;
    page: number;
    total: number;

    sort: INglDatatableSort = {key: 'id', order: 'asc'};

    constructor(private commentService: CommentService) {
    }

    ngOnInit() {
        this.onPageChange();
    }

    onSort($event: INglDatatableSort) {
        const {key, order} = $event;
        this.data.sort((a: any, b: any) => {
            return (key === 'id' ? b[key] - a[key] : b[key].localeCompare(a[key])) * (order === 'desc' ? 1 : -1);
        });
    }

    onRowClick($event: INglDatatableRowClick) {
    }

    onPageChange(pageNumber: number = 1) {
        if (pageNumber < 1)
            return;
        this.commentService.getCommends(pageNumber, 10).subscribe(comments => {
            this.data = comments.content;
            this.page = comments.number + 1;
            this.total = comments.totalElements;
        })
    }

    onDelete(id: number) {
        this.commentService.deleteComment(id).subscribe(response => {
            // if (response.result == 'success')
            //     this.onPageChange(this.page);
            // else
                console.log("error");
        });
    }
}
