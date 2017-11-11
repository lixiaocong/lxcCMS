import {Component, OnInit} from "@angular/core";
import {MatDialogRef} from "@angular/material";

@Component({
    selector: 'app-addtask-dialog',
    templateUrl: './addtask-dialog.component.html',
    styleUrls: ['./addtask-dialog.component.css']
})
export class AddtaskDialogComponent implements OnInit {
    taskType: string;
    urlValue: string;
    metalinkValue: string;
    torrentValue: string[];

    constructor(public dialogRef: MatDialogRef<AddtaskDialogComponent>) {
    }

    ngOnInit() {
        this.taskType = AddTaskInfo.TYPE_TORRENT;
        this.urlValue = 'http://';
        this.metalinkValue = 'megnets://';
        this.torrentValue = [];
    }

    onFileChange($event) {
        this.torrentValue = [];
        let fileNumber = $event.target.files.length;
        for (let i = 0; i < fileNumber; i++) {
            let file: File = $event.target.files[i];
            let myReader: FileReader = new FileReader();
            myReader.onloadend = () => {
                this.torrentValue.push(btoa(myReader.result));
            };
            myReader.readAsBinaryString(file);
        }

        return;
    }

    addTask() {
        console.log(this.torrentValue);
        let taskInfo: AddTaskInfo = null;
        if (this.taskType == AddTaskInfo.TYPE_URL)
            taskInfo = new AddTaskInfo(this.taskType, [this.urlValue]);
        else if (this.taskType == AddTaskInfo.TYPE_METALINK)
            taskInfo = new AddTaskInfo(this.taskType, [this.metalinkValue]);
        else {
            taskInfo = new AddTaskInfo(this.taskType, this.torrentValue);
        }
        this.dialogRef.close(taskInfo);
    }

    cancel() {
        this.dialogRef.close();
    }
}

export class AddTaskInfo {
    static TYPE_URL = 'url';
    static TYPE_METALINK = 'metalink';
    static TYPE_TORRENT = 'torrent';

    taskType: string;
    content: string[];

    constructor(taskType: string, content: string[]) {
        this.taskType = taskType;
        this.content = content;
    }
}
