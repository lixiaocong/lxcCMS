import {Component} from '@angular/core';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    title: string;
    sideBarMode: string;
    isSideBarOpen: boolean;

    ngOnInit(): void {
        this.title = 'Admin Lixiaocong';
        this.isSideBarOpen = false;
    }

    sideBarClicked() {
        this.isSideBarOpen = !this.isSideBarOpen;
    }
}
