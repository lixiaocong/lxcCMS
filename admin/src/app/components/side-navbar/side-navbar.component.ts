import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-side-navbar',
  template: `
    <div *ngFor="let item of menu">
      <div class="sidebar-item">
        <a routerLink={{item.link}}>
          <md-icon>{{item.icon}}</md-icon>
          {{item.text}}
        </a>
      </div>
    </div>
  `,
  styleUrls: ['./side-navbar.component.css']
})
export class SideNavbarComponent implements OnInit {

  menu: Array<MenuItem>;
  constructor() { }

  ngOnInit() {
    this.menu = [
      { text: 'user', icon: 'account_box', link: 'user' },
      { text: 'article', icon: 'book', link: 'article' },
      { text: 'comment', icon: 'comment', link: 'comment' },
      { text: 'downloader', icon: 'cloud_download', link: 'downloader' },
      { text: 'file', icon: 'insert_drive_file', link: 'file' },
    ];
  }

}

class MenuItem {
  link: string;
  text: string;
  icon: string;
}
