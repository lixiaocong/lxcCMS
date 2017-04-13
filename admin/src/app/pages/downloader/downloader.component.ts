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
        this.downloadTasks = [];

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
            let tasks: DownloadTask[] = JSON.parse(event.data);
            tasks.forEach(task => {
                task.isChoosed = false;
                for (let i = 0; i < this.downloadTasks.length; i++) {
                    let oldTask: DownloadTask = this.downloadTasks[i];
                    if (oldTask.id == task.id) {
                        task.isChoosed = oldTask.isChoosed;
                        break;
                    }
                }
            });
            this.downloadTasks = tasks;
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
        let command = new StartTaskCommand();
        this.downloadTasks.filter(task=>task.isChoosed).forEach(task=>command.addId(task.id));
        this.ws.send(JSON.stringify(command));
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
    static START_TASK = 'start-task';

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

class StartTaskCommand extends AdminCommand {
    ids: Array<string>;

    constructor() {
        super(AdminCommand.START_TASK);
        this.ids = [];
    }

    addId(id: string) {
        this.ids.push(id);
    }
}
