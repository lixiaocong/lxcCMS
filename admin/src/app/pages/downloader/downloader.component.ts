import {Component, OnDestroy, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material";
import {DownloadTask} from "./download-task/download-task.component";
import {AddtaskDialogComponent, AddTaskInfo} from "./addtask-dialog/addtask-dialog.component";
import {environment} from "../../../environments/environment";
import {FormatUtil} from "../../utils/FormatUtil";

@Component({
    selector: 'app-downloader',
    templateUrl: './downloader.component.html',
    styleUrls: ['./downloader.component.css']
})
export class DownloaderComponent implements OnInit, OnDestroy {

    ws: WebSocket;
    downloadTasks: DownloadTask[];

    constructor(public dialog: MatDialog) {
    }

    private static fromJson(task2add, taskNew) {
        task2add.id = taskNew.id;
        task2add.name = taskNew.name;
        task2add.status = taskNew.status;
        task2add.isTorrent = taskNew.type == 'TORRENT';
        task2add.totalLength = FormatUtil.formatBytes(taskNew.totalLength);
        task2add.downloadLength = FormatUtil.formatBytes(taskNew.downloadLength);
        task2add.downloadSpeed = FormatUtil.formatBytes(taskNew.downloadSpeed);
        task2add.progressValue = taskNew.downloadLength / taskNew.totalLength * 100;
        task2add.uploadLength = FormatUtil.formatBytes(taskNew.uploadLength);
        task2add.uploadSpeed = FormatUtil.formatBytes(taskNew.uploadSpeed);
    }

    ngOnInit() {
        let host = window.location.host;
        if (!environment.production)
            host = '127.0.0.1';
        let url: string = 'ws://' + host + '/downloader-socket';
        this.ws = new WebSocket(url);
        this.downloadTasks = [];

        this.ws.onclose = event => console.log('close');

        this.ws.onerror = event => {
            console.log(JSON.stringify(event));
            console.log('error');
        };

        this.ws.onmessage = event => {
            let tasksNew = JSON.parse(event.data);

            // remove deleted tasks
            for (let index = 0; index < this.downloadTasks.length; index++) {
                let exists = false;
                for (let indexNew = 0; indexNew < tasksNew.length; indexNew++) {
                    if (tasksNew[indexNew].id == this.downloadTasks[index].id) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    this.downloadTasks.splice(index, 1);
                    index--;
                }
            }

            // update and add new task
            tasksNew.forEach(taskNew => {
                taskNew.isChoosed = false;
                let exists = false;
                for (let index = 0; index < this.downloadTasks.length; index++) {
                    if (this.downloadTasks[index].id == taskNew.id) {
                        DownloaderComponent.fromJson(this.downloadTasks[index], taskNew);
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    let task2add = new DownloadTask();
                    DownloaderComponent.fromJson(task2add, taskNew);
                    this.downloadTasks.push(taskNew);
                }
            });
        };
    }

    ngOnDestroy(): void {
        this.ws.close();
        console.log("closed");
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
        this.downloadTasks.filter(task => task.isChosen).forEach(task => command.addId(task.id));
        this.ws.send(JSON.stringify(command));
    }

    pause_task() {
        let command = new PauseTaskCommand();
        this.downloadTasks.filter(task => task.isChosen).forEach(task => command.addId(task.id));
        this.ws.send(JSON.stringify(command));
    }

    delete_task() {
        let command = new RemoveTaskCommand();
        this.downloadTasks.filter(task => task.isChosen).forEach(task => command.addId(task.id));
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

class PauseTaskCommand extends DownloaderCommand {
    ids: Array<string>;

    constructor() {
        super(DownloaderCommand.PAUSE_TASK);
        this.ids = [];
    }

    addId(id: string) {
        this.ids.push(id);
    }
}

class RemoveTaskCommand extends DownloaderCommand {
    ids: Array<string>;

    constructor() {
        super(DownloaderCommand.REMOVE_TASK);
        this.ids = [];
    }

    addId(id: string) {
        this.ids.push(id);
    }
}
