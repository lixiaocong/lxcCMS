import { Component, OnInit } from '@angular/core';
import { Http, Response, Headers} from "@angular/http"

@Component({
  selector: 'app-downloader',
  templateUrl: './downloader.component.html',
  styleUrls: ['./downloader.component.css']
})
export class DownloaderComponent implements OnInit {
  private http:Http;

  constructor(http:Http) { 
    this.http = http;
  }

  ngOnInit() {
  }

  upload_task(){
    this.http.get('http://localhost:8081/article')
    alert('upload');
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
