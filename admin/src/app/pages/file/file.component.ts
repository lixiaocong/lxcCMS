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

    baseUrl: string;
    path: string;
    files: File[];

    constructor(private fileService: FileService) {
    }

    ngOnInit() {
        let host = window.location.host;
        if (!environment.production)
            host = '127.0.0.1';
        this.baseUrl = 'http://' + host + '/download';
        this.path = '/';
        this.update();
    }

    onDelete(fileName: string) {
        this.fileService.deleteFile(this.path + fileName).subscribe(data => {
            if (data.result == 'success')
                this.update();
            else
                console.log(data.message);
        });
    }

    onOpen(fileName: string) {
        this.path += fileName + '/';
        this.update();
    }

    onBack() {
        console.log(this.path);
        let name = this.path.split("/");
        let length = name.length;
        if (length <= 2)
            this.path = '/';
        else {
            this.path = '/';
            // console.log(name);
            for (let i = 1; i < length - 2; i++)
                this.path += name[i] + '/';
        }
        // console.log(this.path);
        this.update();
    }

    private update() {
        this.fileService.getFiles(this.path).subscribe(data => {
            this.files = data.files;
            this.files.sort((a: File, b: File) => {
                if (a.file != b.file) {
                    if (a.file)
                        return 1;
                    return -1;
                }
                return 0;
            });
        });
    }
}

class File {
    name: string;
    file: boolean;
}