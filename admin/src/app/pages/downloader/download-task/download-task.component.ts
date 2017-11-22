import {Component, Input, OnInit} from "@angular/core";

@Component({
    selector: 'download-task',
    templateUrl: './download-task.component.html',
    styleUrls: ['./download-task.component.css']
})
export class DownloadTaskComponent implements OnInit {
    @Input() downloadTask: DownloadTask;

    styleClass: string;
    progressColor: string;
    progressMode: string;

    constructor() {
    }

    ngOnInit() {
        if (this.downloadTask.isChosen)
            this.styleClass = 'clickedComponent';
        else
            this.styleClass = 'component';
        this.progressColor = 'primary';
        this.progressMode = 'determinate';
    }

    onHover() {
        this.styleClass = 'hoveredComponent';
    }

    onLeave() {
        if (this.downloadTask.isChosen)
            this.styleClass = 'clickedComponent';
        else
            this.styleClass = 'component';
    }

    onClick() {
        this.downloadTask.isChosen = !this.downloadTask.isChosen;
        if (this.downloadTask.isChosen)
            this.styleClass = 'clickedComponent';
        else
            this.styleClass = 'component';
    }
}

export class DownloadTask {
    isChosen: boolean;

    id: string;
    isTorrent: boolean;
    progressValue: number;
    status: string;
    name: string;
    totalLength: string;
    downloadLength: string;
    downloadSpeed: string;
    uploadLength: string;
    uploadSpeed: string;

    DownloadTask() {
        this.isChosen = false;
    }
}
