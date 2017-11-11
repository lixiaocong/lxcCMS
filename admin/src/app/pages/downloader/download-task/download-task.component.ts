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
    progressValue: number;

    isTorrent: boolean;
    totalLength: string;
    downloadLength: string;
    downloadSpeed: string;
    uploadLength: string;
    uploadSpeed: string;

    constructor() {
    }

    ngOnInit() {
        if (this.downloadTask.isChoosed)
            this.styleClass = 'clickedComponent';
        else
            this.styleClass = 'component';

        this.progressColor = 'primary';
        this.progressMode = 'determinate';
        this.progressValue = this.downloadTask.downloadLength / this.downloadTask.totalLength * 100;

        this.isTorrent = this.downloadTask.type == "TORRENT";
        this.totalLength = DownloadTaskComponent.formatBytes(this.downloadTask.totalLength);
        this.downloadLength = DownloadTaskComponent.formatBytes(this.downloadTask.downloadLength);
        this.downloadSpeed = DownloadTaskComponent.formatBytes(this.downloadTask.downloadSpeed);
        this.uploadLength = DownloadTaskComponent.formatBytes(this.downloadTask.uploadLength);
        this.uploadSpeed = DownloadTaskComponent.formatBytes(this.downloadTask.uploadSpeed);
    }

    onHover() {
        this.styleClass = 'hoveredComponent';
    }

    onLeave() {
        if (this.downloadTask.isChoosed)
            this.styleClass = 'clickedComponent';
        else
            this.styleClass = 'component';
    }

    onClick() {
        this.downloadTask.isChoosed = !this.downloadTask.isChoosed;
        if (this.downloadTask.isChoosed)
            this.styleClass = 'clickedComponent';
        else
            this.styleClass = 'component';
    }

    private static formatBytes(bytes: number, decimals: number = 2) {
        if (bytes == 0) return '0 Bytes';
        let k = 1000,
            dm = decimals + 1 || 3,
            sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
            i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
    }
}

//下载任务的结构
class DownloadFile {
    name: string;
    path: string;
    totalLength: number;
    downloadLength: number;
}

export class DownloadTask {
    isChoosed: boolean;

    id: string;
    status: string;
    type: string;
    name: string;
    totalLength: number;
    downloadLength: number;
    downloadSpeed: number;
    uploadLength: number;
    uploadSpeed: number;
    dir :string;
    files : DownloadFile[];
}
