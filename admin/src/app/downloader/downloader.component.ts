import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-downloader',
  templateUrl: './downloader.component.html',
  styleUrls: ['./downloader.component.css']
})
export class DownloaderComponent implements OnInit {
  ws:WebSocket ;

  ngOnInit() {
    let url:string = 'ws://localhost:8081/socket';
    this.ws = new WebSocket(url);

    this.ws.onopen = event => {
      console.log('open');
    };
    this.ws.onclose = event =>{
      console.log('close');
    };
    this.ws.onerror = event =>{
      console.log('error');
    };
    this.ws.onmessage = event =>{
      console.log(event.data);
    }
  }

  upload_task(){
    this.ws.send("hello")
  }

  start_task(){
    alert("start")
  }

  pause_task(){
    alert("pause")
  }

  delete_task(){
    alert("delete")
  }
}
