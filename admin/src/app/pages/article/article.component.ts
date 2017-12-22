import {Component, OnInit} from "@angular/core";
import {ArticleService} from "./article.service";
import {MatTableDataSource, PageEvent} from "@angular/material";

@Component({
    selector: 'app-article',
    templateUrl: './article.component.html',
    styleUrls: ['./article.component.css'],
    providers: [ArticleService]
})
export class ArticleComponent implements OnInit {

    displayedColumns = ['id', 'title', 'action'];
    dataSource = new MatTableDataSource();
    page: number;
    total: number;

    constructor(private articleService: ArticleService) {
    }

    ngOnInit(): void {
        this.onPageChange();
    }

    onPage(page: PageEvent) {
        this.onPageChange(page.pageIndex)
    }

    onPageChange(pageNumber: number = 1) {
        if (pageNumber < 1)
            return;
        this.articleService.getArticles(pageNumber, 10).subscribe(articles => {
            this.dataSource.data = articles.content;
            this.page = articles.number + 1;
            this.total = articles.totalElements;
        })
    }

    onDelete(id: number) {
        this.articleService.deleteArticle(id).subscribe(response => {
            if (response.result == 'success')
                this.onPageChange(this.page);
            else
                console.log("error");
        });
    }
}
