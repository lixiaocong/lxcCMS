import { Component, OnInit } from '@angular/core';
import { MdDialogRef } from '@angular/material';

@Component({
  selector: 'app-addtask-dialog',
  templateUrl: './addtask-dialog.component.html',
  styleUrls: ['./addtask-dialog.component.css']
})
export class AddtaskDialogComponent implements OnInit {
  taskType: string;
  urlValue: string;
  metalinkValue: string;
  torrentValue: string;

  constructor(public dialogRef: MdDialogRef<AddtaskDialogComponent>) { }

  ngOnInit() {
    this.taskType = 'torrent';
    this.urlValue = 'http://';
    this.metalinkValue = 'megnets://';
    this.torrentValue = null;
  }

  onFileChange($event) {
    var file: File = $event.target.files[0];
    if (file == null) {
      this.torrentValue = null;
      return;
    }
    var myReader: FileReader = new FileReader();
    myReader.onloadend = () => {
      this.torrentValue = myReader.result;
    }
    myReader.readAsText(file);
  }

  addTask() {
    let taskInfo: AddTaskInfo = null;
    if (this.taskType === 'url')
      taskInfo = new AddTaskInfo('url', this.urlValue);
    else if (this.taskType === 'metalink')
      taskInfo = new AddTaskInfo('metalink', this.metalinkValue);
    else {
      taskInfo = new AddTaskInfo('torrent', this.torrentValue);

    }
    this.dialogRef.close(taskInfo);
  }

  cancel() {
    this.dialogRef.close();
  }
}

export class AddTaskInfo {
  constructor(taskType: string, content: string) {
    this.taskType = taskType;
    this.content = content;
  }

  taskType: string;
  content: string;
}
