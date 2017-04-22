import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { routes } from './app.router';
import { MaterialModule } from '@angular/material';

import { AppComponent } from './app.component';
import { SideNavbarComponent } from './components/side-navbar/side-navbar.component';
import { SideNavbarItemComponent } from './components/side-navbar-item/side-navbar-item.component';
import { UserComponent } from './pages/user/user.component';
import { ArticleComponent } from './pages/article/article.component';
import { CommentComponent } from './pages/comment/comment.component';
import { DownloaderComponent } from './pages/downloader/downloader.component';
import { DownloadTaskComponent } from './pages/downloader/download-task/download-task.component';
import { FileComponent } from './pages/file/file.component';
import { AddtaskDialogComponent } from './pages/downloader/addtask-dialog/addtask-dialog.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { DashboardCardComponent } from './pages/dashboard/dashboard-card/dashboard-card.component';
import { UserItemComponent } from './pages/user/user-item/user-item.component';

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
    SideNavbarItemComponent,
    AddtaskDialogComponent,
    DashboardComponent,
    DashboardCardComponent,
    UserItemComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    routes,
    MaterialModule,
  ],
  providers: [],
  entryComponents: [AddtaskDialogComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }
