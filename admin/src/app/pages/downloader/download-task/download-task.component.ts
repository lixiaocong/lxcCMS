import { Component, OnInit, Input } from '@angular/core';

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

  speed: string;
  total: string;

  constructor() { }

  ngOnInit() {
    if (this.downloadTask.isChoosed)
      this.styleClass = 'clickedComponent';
    else
      this.styleClass = 'component';

    this.progressColor = 'primary';
    this.progressMode = 'determinate';
    this.speed = DownloadTaskComponent.formatBytes(this.downloadTask.speed);
    this.total = DownloadTaskComponent.formatBytes(this.downloadTask.totalLength);
    this.progressValue = this.downloadTask.downloadedLength / this.downloadTask.totalLength * 100;
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
export class DownloadTask {
  isChoosed:boolean;

  id: string;
  downloadType: string;
  name: string;
  totalLength: number;
  downloadedLength: number;
  speed: number;
  status: string;
  finished: boolean;
}
