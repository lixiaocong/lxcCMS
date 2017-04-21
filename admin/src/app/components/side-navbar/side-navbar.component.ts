import { Component, OnInit } from '@angular/core';
import { SideNavbarItemComponent, NavbarItem } from '../side-navbar-item/side-navbar-item.component';

@Component({
  selector: 'app-side-navbar',
  template: `
    <div>
    <div *ngFor="let navbarItem of navbarItems">
      <app-side-navbar-item [navbarItem] = navbarItem></app-side-navbar-item> 
    </div>
    </div>
  `,
  styles: [``]
})
export class SideNavbarComponent implements OnInit {
  navbarItems: Array<NavbarItem>;

  ngOnInit() {
    this.navbarItems = [
      new NavbarItem('dashboard', 'dashboard', 'dashboard'),
      new NavbarItem('article', 'book', 'article'),
      new NavbarItem('user', 'account_box', 'user'),
      new NavbarItem('comment', 'comment', 'comment'),
      new NavbarItem('downloader', 'cloud_download', 'downloader'),
      new NavbarItem('file', 'insert_drive_file', 'file'),
    ];
  }
}