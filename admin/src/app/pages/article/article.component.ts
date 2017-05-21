import { Component, OnInit } from '@angular/core';
import {INglDatatableSort, INglDatatableRowClick} from 'ng-lightning/ng-lightning';

const DATA = [
  { rank: 1, name: 'Kareem', surname: 'Abdul-Jabbar', points: 38387 },
  { rank: 2, name: 'Karl', surname: 'Malone', points: 36928 },
  { rank: 3, name: 'Kobe', surname: 'Bryant', points: 33643 },
  { rank: 4, name: 'Michael', surname: 'Jordan', points: 32292 },
  { rank: 5, name: 'Wilt', surname: 'Chamberlain', points: 31419 },
];
@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.css']
})
export class ArticleComponent {

  data = DATA;

  // Initial sort
  sort: INglDatatableSort = { key: 'rank', order: 'asc' };

  // Show loading overlay
  loading = false;

  // Toggle name column
  hideName = false;

  // Custom sort function
  onSort($event: INglDatatableSort) {
    const { key, order } = $event;
    this.data.sort((a: any, b: any) => {
      return (key === 'rank' ? b[key] - a[key] : b[key].localeCompare(a[key])) * (order === 'desc' ? 1 : -1);
    });
  }

  toggleData() {
    this.data = this.data ? null : DATA;
  }

  onRowClick($event: INglDatatableRowClick) {
    console.log('clicked row', $event.data);
  }
}
