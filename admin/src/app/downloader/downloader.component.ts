import {Component, OnInit} from '@angular/core';

import {DownloadTask, DownloadTaskComponent} from './download-task/download-task.component'

@Component({
  selector: 'app-downloader',
  templateUrl: './downloader.component.html',
  styleUrls: ['./downloader.component.css']
})
export class DownloaderComponent implements OnInit {
  ws: WebSocket;
  private downloadTasks: DownloadTask[];

  ngOnInit() {
    let url: string = 'ws://localhost:8080/socket';
    this.ws = new WebSocket(url);

    this.ws.onopen = event => {
      console.log('open');
    };
    this.ws.onclose = event => {
      console.log('close');
    };
    this.ws.onerror = event => {
      console.log('error');
    };
    this.ws.onmessage = event => {
      this.downloadTasks = JSON.parse(event.data);
    };

    setInterval(() => {
      let command = new AdminCommand();
      command.method = AdminCommand.GET_DOWNLOAD_TASK;
      this.ws.send(JSON.stringify(command));
    }, 1000 * 5);
  }

  upload_task() {
    alert("upload")
  }

  start_task() {
    alert("start")
  }

  pause_task() {
    alert("pause")
  }

  delete_task() {
    alert("delete")
  }
}

//命令的结构
class AdminCommand {
  static GET_DOWNLOAD_TASK = 'get-download-task';
  method: string;
}