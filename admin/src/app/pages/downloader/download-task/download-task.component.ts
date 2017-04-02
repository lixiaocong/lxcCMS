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

  private isClicked: boolean;

  constructor() { }

  ngOnInit() {
    this.styleClass = 'component';

    this.progressColor = 'primary';
    this.progressMode = 'determinate';
    this.speed = this.formatBytes(this.downloadTask.speed);
    this.total = this.formatBytes(this.downloadTask.totalLength);
    this.progressValue = this.downloadTask.downloadedLength / this.downloadTask.totalLength * 100;

    this.isClicked = false;
  }

  onHover() {
    this.styleClass = 'hoveredComponent';
  }

  onLeave() {
    if (this.isClicked)
      this.styleClass = 'clickedComponent';
    else
      this.styleClass = 'component';
  }

  onClick() {
    this.isClicked = !this.isClicked;
    if (this.isClicked)
      this.styleClass = 'clickedComponent';
    else
      this.styleClass = 'component';
  }

  private formatBytes(bytes: number, decimals: number = 2) {
    if (bytes == 0) return '0 Bytes';
    var k = 1000,
      dm = decimals + 1 || 3,
      sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
      i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
  }
}

//下载任务的结构
export class DownloadTask {
  id: string;
  downloadType: string;
  name: string;
  totalLength: number;
  downloadedLength: number;
  speed: number;
  status: string;
  finished: boolean;
}
