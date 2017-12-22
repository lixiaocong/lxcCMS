import {Component, Input, OnInit} from "@angular/core";

@Component({
    selector: 'app-side-navbar-item',
    templateUrl: './side-navbar-item.component.html',
    styleUrls: ['./side-navbar-item.component.css']
})
export class SideNavbarItemComponent implements OnInit {
    @Input() navbarItem: NavbarItem;

    styleClass: string;

    ngOnInit() {
        this.styleClass = 'sidebar-item';
    }

    onHover() {
        this.styleClass = 'sidebar-item-hover';
    }

    onLeave() {
        this.styleClass = 'sidebar-item';
    }
}

export class NavbarItem {

    link: string;
    text: string;
    icon: string;

    constructor(link: string, icon: string, text: string,) {
        this.link = link;
        this.icon = icon;
        this.text = text;
    }
}