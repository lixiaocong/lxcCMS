import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgModule} from '@angular/core';
import {MatSlideToggleModule, MatExpansionModule, MatCheckboxModule , MatButtonModule} from '@angular/material'
import {NglModule} from 'ng-lightning/ng-lightning'
import {FormsModule} from "@angular/forms";
import {routes} from "./app.router";

import {AppComponent} from './app.component';
import {SideNavbarComponent} from "./components/side-navbar/side-navbar.component";
import {SideNavbarItemComponent} from "./components/side-navbar-item/side-navbar-item.component";
import {UserComponent} from "./pages/user/user.component";
import {ArticleComponent} from "./pages/article/article.component";
import {CommentComponent} from "./pages/comment/comment.component";
import {DownloaderComponent} from "./pages/downloader/downloader.component";
import {DownloadTaskComponent} from "./pages/downloader/download-task/download-task.component";
import {FileComponent} from "./pages/file/file.component";
import {AddtaskDialogComponent} from "./pages/downloader/addtask-dialog/addtask-dialog.component";
import {DashboardComponent} from "./pages/dashboard/dashboard.component";
import {DashboardCardComponent} from "./pages/dashboard/dashboard-card/dashboard-card.component";
import {UserItemComponent} from "./pages/user/user-item/user-item.component";
import {ConfigComponent} from "./pages/config/config.component";
import {HashLocationStrategy, LocationStrategy} from "@angular/common";

import {
    MatCardModule,
    MatGridListModule,
    MatInputModule,
    MatRadioModule,
    MatSidenavModule,
    MatProgressBarModule,
    MatIconModule, MatToolbarModule, MatDialogModule
} from '@angular/material'
import {HttpClientModule} from "@angular/common/http";
import 'rxjs/Rx';

@NgModule({
    declarations: [
        AppComponent,
        UserComponent,
        ArticleComponent,
        CommentComponent,
        DownloaderComponent,
        FileComponent,
        DownloadTaskComponent,
        ConfigComponent,
        SideNavbarComponent,
        SideNavbarItemComponent,
        AddtaskDialogComponent,
        DashboardComponent,
        DashboardCardComponent,
        UserItemComponent,
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        MatSidenavModule,
        MatProgressBarModule,
        MatIconModule,
        MatRadioModule,
        MatInputModule,
        MatGridListModule,
        MatCardModule,
        MatToolbarModule,
        MatCheckboxModule,
        MatButtonModule,
        MatDialogModule,
        MatToolbarModule,
        MatExpansionModule,
        MatSlideToggleModule,
        HttpClientModule,
        FormsModule,
        routes,
        NglModule.forRoot({
            svgPath: 'assets'
        }),
    ],
    providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}],
    entryComponents: [AddtaskDialogComponent],
    bootstrap: [AppComponent]
})
export class AppModule {
}
