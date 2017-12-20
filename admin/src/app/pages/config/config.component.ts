import {Component, OnInit} from "@angular/core";
import {ConfigService} from "./config.service";
import {MatSlideToggleChange } from '@angular/material/slide-toggle'
@Component({
    selector: 'app-config',
    templateUrl: './config.component.html',
    styleUrls: ['./config.component.css'],
    providers: [ConfigService]
})
export class ConfigComponent implements OnInit {
    private config = new Config();

    constructor(private configService: ConfigService) {
    }

    ngOnInit(): void {
        this.configService.getConfig().subscribe(config=>{
            this.config.blogEnabled = config.blogEnabled;
        });
    }

    onBlogChange(change: MatSlideToggleChange){
        this.configService.setConfigValue("blogEnabled",change.checked?"1":"0").subscribe();
    }
}

export class Config{
    blogEnabled: boolean;

    applicationUrl: string;
    qqEnabled: boolean;
    qqId: string;
    qqSecret: string;

    downloaderEnabled: boolean;
    aria2cUrl: string;
    aria2cPassword: string;
    transmissionUrl: string;
    transmissionUsername: string;
    transmissionPassword: string;

    weixinEnabled: boolean;
    weixinId: string;
    weixinSecret: string;
    weixinToken: string;
    weixinKey: string;
}
