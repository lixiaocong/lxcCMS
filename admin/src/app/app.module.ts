import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { UserComponent } from './pages/user/user.component';
import { ArticleComponent } from './pages/article/article.component';
import { CommentComponent } from './pages/comment/comment.component';
import { DownloaderComponent } from './pages/downloader/downloader.component';
import { FileComponent } from './pages/file/file.component';
import { DownloadTaskComponent } from './pages/downloader/download-task/download-task.component';
import { SideNavbarComponent } from './components/side-navbar/side-navbar.component';

import { routes } from './app.router';
import { MaterialModule } from '@angular/material';

@NgModule({
  declarations: [
    AppComponent,
    UserComponent,
    ArticleComponent,
    CommentComponent,
    DownloaderComponent,
    FileComponent,
    DownloadTaskComponent,
    SideNavbarComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    routes,
    MaterialModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
