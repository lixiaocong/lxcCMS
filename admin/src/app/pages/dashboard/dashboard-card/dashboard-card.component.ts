import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-dashboard-card',
  templateUrl: './dashboard-card.component.html',
  styleUrls: ['./dashboard-card.component.css']
})
export class DashboardCardComponent implements OnInit {
  @Input() dashboardCard:DashboardItem;

  constructor() { }

  ngOnInit() {
  }

}

export class DashboardItem{
  title: string;
  content: string;

  constructor(title:string,content:string){
    this.title = title;
    this.content = content;
  }
}
