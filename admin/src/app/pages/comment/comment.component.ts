import {Component, OnInit} from "@angular/core";
import {CommentService} from "./comment.service";
import {MatTableDataSource, PageEvent} from "@angular/material"

@Component({
    selector: 'app-comment',
    templateUrl: './comment.component.html',
    styleUrls: ['./comment.component.css'],
    providers: [CommentService]
})
export class CommentComponent implements OnInit {

    displayedColumns = ['id', 'content', 'action'];
    dataSource = new MatTableDataSource();
    page: number;
    total: number;


    constructor(private commentService: CommentService) {
    }

    ngOnInit() {
        this.onPageChange();
    }

    onPage(page: PageEvent) {
        this.onPageChange(page.pageIndex)
    }

    onPageChange(pageNumber: number = 1) {
        if (pageNumber < 1)
            return;
        this.commentService.getCommends(pageNumber, 10).subscribe(comments => {
            this.dataSource.data = comments.content;
            this.page = comments.number + 1;
            this.total = comments.totalElements;
        })
    }

    onDelete(id: number) {
        this.commentService.deleteComment(id).subscribe(response => {
            if (response.result == 'success')
                this.onPageChange(this.page);
            else
                console.log("error");
        });
    }
}
