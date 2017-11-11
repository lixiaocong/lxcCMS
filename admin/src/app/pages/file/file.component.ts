import {Component, OnInit} from "@angular/core";
import {FileService} from "./file.service";
import {environment} from "../../../environments/environment";

@Component({
    selector: 'app-file',
    templateUrl: './file.component.html',
    styleUrls: ['./file.component.css'],
    providers: [FileService]
})
export class FileComponent implements OnInit {

    baseUrl;
    videos;
    constructor(private fileService: FileService) {
    }

    ngOnInit() {
        let host = window.location.host;
        if (!environment.production)
            host = '127.0.0.1';
        this.baseUrl = 'http://' + host + '/download/';
        this.update();
    }


    onDelete(fileName: string) {
        this.fileService.deleteFile(fileName).subscribe(data => {
            if (data.result == 'success')
                this.update();
            else
                console.log(data.message);
        });
    }


    private update() {
        this.fileService.getFiles().subscribe(data => {
            this.videos = data.videos;
        });
    }
}
