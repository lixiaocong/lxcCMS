import {Component, OnInit} from "@angular/core";
import {FileService} from "./file.service";
import {environment} from "../../../environments/environment";
import {MatTableDataSource} from "@angular/material";
import {FormatUtil} from "../../utils/FormatUtil";

@Component({
    selector: 'app-file',
    templateUrl: './file.component.html',
    styleUrls: ['./file.component.css'],
    providers: [FileService]
})
export class FileComponent implements OnInit {

    baseUrl: string;
    path: string;
    freeSpace: string;
    files = new MatTableDataSource<File>();
    displayedColumns = ['name', 'size', 'action'];

    constructor(private fileService: FileService) {
    }

    ngOnInit() {
        let host = window.location.host;
        if (!environment.production)
            host = '127.0.0.1';
        this.baseUrl = 'http://' + host + '/downloads';
        this.path = '/';
        this.freeSpace = '-';
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
            for (let i = 1; i < length - 2; i++)
                this.path += name[i] + '/';
        }
        this.update();
    }

    private update() {
        this.fileService.getFiles(this.path).subscribe(data => {
            this.files.data = data.files.sort((a: File, b: File) => {
                if (a.file != b.file) {
                    console.log(a.name + " " + b.name);
                    if (a.file)
                        return 1;
                    else
                        return -1;
                }
                else {
                    if (a.name.toLowerCase() < b.name.toLowerCase())
                        return -1;
                    else if (a.name.toLowerCase() > b.name.toLowerCase())
                        return 1;
                    else
                        return 0;
                }
            });
        });
        this.fileService.getSpace().subscribe(data => {
            this.freeSpace = FormatUtil.formatBytes(data.space);
        });
    }

    private format(size: number): string {
        if (size > 0)
            return FormatUtil.formatBytes(size);
        return '-';
    }
}

class File {
    name: string;
    file: boolean;
    size: number;
}