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
  menu: Array<MenuItem>;

  ngOnInit(): void {
    this.title = 'Admin Lixiaocong';
    this.sideBarMode = 'push';
    this.isSideBarOpen = false;
    this.menu = [
      {text: 'user', icon: 'account_box', link: 'user'},
      {text: 'article', icon: 'book', link: 'article'},
      {text: 'comment', icon: 'comment', link: 'comment'},
      {text: 'downloader', icon: 'cloud_download', link: 'downloader'},
      {text: 'file', icon: 'insert_drive_file', link: 'file'},
    ];
  }

  sideBarClicked() {
    this.isSideBarOpen = !this.isSideBarOpen;
  }
}

class MenuItem {
  link: string;
  text: string;
  icon: string;
}
