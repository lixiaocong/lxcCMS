import {Component, OnInit} from "@angular/core";
import {MdDialog} from "@angular/material";
import {DownloadTask} from "./download-task/download-task.component";
import {AddtaskDialogComponent, AddTaskInfo} from "./addtask-dialog/addtask-dialog.component";

@Component({
    selector: 'app-downloader',
    templateUrl: './downloader.component.html',
    styleUrls: ['./downloader.component.css']
})
export class DownloaderComponent implements OnInit {
    ws: WebSocket;
    private downloadTasks: DownloadTask[];

    constructor(public dialog: MdDialog) {
    }

    ngOnInit() {
        let url: string = 'ws://localhost:8080/socket';
        this.ws = new WebSocket(url);

        this.ws.onopen = event => {
            this.get_task();
            setInterval(() => this.get_task(), 1000);
        };
        this.ws.onclose = event => {
            console.log('close');
            console.log(event.code)
        };
        this.ws.onerror = event => {
            console.log('error');
        };
        this.ws.onmessage = event => {
            this.downloadTasks = JSON.parse(event.data);
        };
    }

    private get_task() {
        this.ws.send(JSON.stringify(new GetTaskCommand()));
    }

    upload_task() {
        let dialogRef = this.dialog.open(AddtaskDialogComponent);
        dialogRef.afterClosed().subscribe(result => {
            console.log(JSON.stringify(new AddTaskCommand(result)));
            if (result != null)
                this.ws.send(JSON.stringify(new AddTaskCommand(result)));
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
    static GET_TASK = 'get-task';
    static ADD_TASK = 'add-task';

    method: string;

    constructor(method: string) {
        this.method = method;
    }
}

class GetTaskCommand extends AdminCommand {
    constructor() {
        super(AdminCommand.GET_TASK);
    }
}
class AddTaskCommand extends AdminCommand {
    addTaskInfo: AddTaskInfo;

    constructor(addTaskInfo: AddTaskInfo) {
        super(AdminCommand.ADD_TASK);
        this.addTaskInfo = addTaskInfo;
    }
}