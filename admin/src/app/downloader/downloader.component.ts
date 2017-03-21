import { Component, OnInit } from '@angular/core';
import {Http, Response} from '@angular/http';
import 'rxjs/add/operator/toPromise'
@Component({
  selector: 'app-downloader',
  templateUrl: './downloader.component.html',
  styleUrls: ['./downloader.component.css']
})
export class DownloaderComponent implements OnInit {
  url:string = 'http://localhost:8081/article?page=1';

  constructor(private http:Http) {}

  ngOnInit() {
  }

  upload_task(){
    this.http.get(this.url).map(this.extractData).toPromise();
  }

  private extractData(rep:Response) {
    console.log(rep.text());
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
