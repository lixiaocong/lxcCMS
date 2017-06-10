import {Component, OnInit} from '@angular/core';
import {FileService} from "./file.service";

@Component({
    selector: 'app-file',
    templateUrl: './file.component.html',
    styleUrls: ['./file.component.css'],
    providers: [FileService]
})
export class FileComponent implements OnInit {

    private url;
    private videos;

    constructor(private fileService: FileService) {
    }

    ngOnInit() {
        this.update();
    }

    onDelete(fileName: string) {
        this.fileService.deleteFile(fileName).subscribe(response=>{
            if(response.result == 'success')
                this.update();
            else
                console.log("error");
        });
    }

    private update(){
        this.fileService.getFiles().subscribe(data => {
            this.videos = data.videos;
            this.url = data.serverUrl;
        });
    }
}
