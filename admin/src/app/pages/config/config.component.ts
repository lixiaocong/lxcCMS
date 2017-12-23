import {Component, OnInit} from "@angular/core";
import {ConfigService} from "./config.service";

@Component({
    selector: 'app-config',
    templateUrl: './config.component.html',
    styleUrls: ['./config.component.css'],
    providers: [ConfigService]
})
export class ConfigComponent implements OnInit {
    config = new Config();

    constructor(private configService: ConfigService) {
    }

    ngOnInit(): void {
        this.configService.getConfig().subscribe(config => {
            this.config = config
        });
    }

    onBlogSave() {
        this.configService.setConfigValue([
            {key: "blogEnabled", value: this.config.blogEnabled ? "1" : "0"},
        ]).subscribe();
    }

    onQQSave() {
        this.configService.setConfigValue([
            {key: "qqEnabled", value: this.config.qqEnabled ? "1" : "0"},
            {key: "applicationUrl", value: this.config.applicationUrl},
            {key: "qqSecret", value: this.config.qqSecret},
            {key: "qqId", value: this.config.qqId},
        ]).subscribe();
    }

    onWeixinSave() {
        this.configService.setConfigValue([
            {key: "weixinEnabled", value: this.config.qqEnabled ? "1" : "0"},
            {key: "weixinId", value: this.config.weixinId},
            {key: "weixinSecret", value: this.config.weixinSecret},
            {key: "weixinToken", value: this.config.weixinToken},
            {key: "weixinKey", value: this.config.weixinKey},
        ]).subscribe();
    }

    onDownloaderSave() {
        this.configService.setConfigValue([
            {key: "downloaderEnabled", value: this.config.downloaderEnabled ? "1" : "0"},
            {key: "aria2cUrl", value: this.config.aria2cUrl},
            {key: "aria2cPassword", value: this.config.aria2cPassword},
            {key: "transmissionUrl", value: this.config.transmissionUrl},
            {key: "transmissionUsername", value: this.config.transmissionUsername},
            {key: "transmissionPassword", value: this.config.transmissionPassword},
        ]).subscribe();
    }
}

export class Config {
    blogEnabled: boolean;

    qqEnabled: boolean;
    applicationUrl: string;
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
