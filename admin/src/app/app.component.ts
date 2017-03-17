import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title: string = 'Admin Lixiaocong';
  
  sideBarMode: string = 'over';
  isSideBarOpen: boolean = false;

  sideBarClicked(){
      this.isSideBarOpen = !this.isSideBarOpen;
  }
}
