import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title: string;
  sideBarMode: string;
  isSideBarOpen: boolean;

  ngOnInit(): void {
    this.title = 'Admin Lixiaocong';
    this.sideBarMode = 'push';
    this.isSideBarOpen = false;
  }

  sideBarClicked() {
    this.isSideBarOpen = !this.isSideBarOpen;
  }
}