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
        let url: string = 'ws://localhost:8080/downloader-socket';
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
        let command = new PauseTaskCommand();
        this.downloadTasks.filter(task=>task.isChoosed).forEach(task=>command.addId(task.id));
        this.ws.send(JSON.stringify(command));
    }

    delete_task() {
        let command = new RemoveTaskCommand();
        this.downloadTasks.filter(task=>task.isChoosed).forEach(task=>command.addId(task.id));
        this.ws.send(JSON.stringify(command));
    }
}

//命令的结构
class DownloaderCommand {
    static GET_TASK = 'get-task';
    static ADD_TASK = 'add-task';
    static START_TASK = 'start-task';
    static PAUSE_TASK = 'pause-task';
    static REMOVE_TASK = 'remove-task';

    method: string;

    constructor(method: string) {
        this.method = method;
    }
}

class GetTaskCommand extends DownloaderCommand {
    constructor() {
        super(DownloaderCommand.GET_TASK);
    }
}
class AddTaskCommand extends DownloaderCommand {
    addTaskInfo: AddTaskInfo;

    constructor(addTaskInfo: AddTaskInfo) {
        super(DownloaderCommand.ADD_TASK);
        this.addTaskInfo = addTaskInfo;
    }
}

class StartTaskCommand extends DownloaderCommand {
    ids: Array<string>;

    constructor() {
        super(DownloaderCommand.START_TASK);
        this.ids = [];
    }

    addId(id: string) {
        this.ids.push(id);
    }
}

class PauseTaskCommand extends DownloaderCommand{
    ids: Array<string>;

    constructor() {
        super(DownloaderCommand.PAUSE_TASK);
        this.ids = [];
    }

    addId(id: string) {
        this.ids.push(id);
    }
}

class RemoveTaskCommand extends DownloaderCommand{
    ids: Array<string>;

    constructor() {
        super(DownloaderCommand.REMOVE_TASK);
        this.ids = [];
    }

    addId(id: string) {
        this.ids.push(id);
    }
}
