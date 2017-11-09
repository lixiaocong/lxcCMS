import {Component, OnInit} from "@angular/core";
import {FileService} from "./file.service";
import {UTF8} from "../../utils/UTF8";

@Component({
    selector: 'app-file',
    templateUrl: './file.component.html',
    styleUrls: ['./file.component.css'],
    providers: [FileService]
})
export class FileComponent implements OnInit {

    url;
    videos;
    constructor(private fileService: FileService) {
    }

    ngOnInit() {
        this.update();
    }

    onDelete(fileName: string) {
        this.fileService.deleteFile(fileName).subscribe(response => {
            // if (response.result == 'success')
            //     this.update();
            // else
            //     console.log(response.message);
        });
    }

    private update() {
        this.fileService.getFiles().subscribe(data => {
            this.videos = data.videos.filter(fileName => {
                try {
                    fileName = this.replaceSpecialChar(fileName);
                    UTF8.b64DecodeUnicode(fileName);
                    return true;
                } catch (e) {
                    return false;
                }
            });
            this.url = data.serverUrl;
        });
    }

    private getName(fileName: string) {
        fileName = this.replaceSpecialChar(fileName);
        fileName = UTF8.b64DecodeUnicode(fileName);
        return fileName;
    }

    private replaceSpecialChar(fileName: string) {
        let ret: string = "";
        for (let i = 0; i < fileName.length; i++) {
            if (fileName.charAt(i) == '_')
                ret += '/';
            else if(fileName.charAt(i) == '-')
                ret += '=';
            else
                ret += fileName.charAt(i);
        }
        return ret;
    }
}
