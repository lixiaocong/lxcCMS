import { Component, OnInit } from '@angular/core';
import { MdDialog, MdDialogRef } from '@angular/material';
import { DownloadTask, DownloadTaskComponent } from './download-task/download-task.component';
import { AddtaskDialogComponent } from './addtask-dialog/addtask-dialog.component';

@Component({
  selector: 'app-downloader',
  templateUrl: './downloader.component.html',
  styleUrls: ['./downloader.component.css']
})
export class DownloaderComponent implements OnInit {
  ws: WebSocket;
  private downloadTasks: DownloadTask[];

  constructor(public dialog: MdDialog) { }

  ngOnInit() {
    let url: string = 'ws://localhost:8080/socket';
    this.ws = new WebSocket(url);

    this.ws.onopen = event => {
      this.get_task();
      setInterval(this.get_task(), 1000 * 5);
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
  }

  private get_task() {
    let command = new AdminCommand();
    command.method = AdminCommand.GET_DOWNLOAD_TASK;
    this.ws.send(JSON.stringify(command));
  }

  upload_task() {
    let dialogRef = this.dialog.open(AddtaskDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result != null)
        console.log(result);
    });
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