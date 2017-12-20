import {ModuleWithProviders} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";

import {UserComponent} from "./pages/user/user.component";
import {ArticleComponent} from "./pages/article/article.component";
import {CommentComponent} from "./pages/comment/comment.component";
import {DownloaderComponent} from "./pages/downloader/downloader.component";
import {FileComponent} from "./pages/file/file.component";
import {DashboardComponent} from "./pages/dashboard/dashboard.component";
import {ConfigComponent} from "./pages/config/config.component";

export const router: Routes = [
    {path: 'user', component: UserComponent},
    {path: 'article', component: ArticleComponent},
    {path: 'comment', component: CommentComponent},
    {path: 'downloader', component: DownloaderComponent},
    {path: 'file', component: FileComponent},
    {path: 'dashboard', component: DashboardComponent},
    {path: 'config', component: ConfigComponent},
    {path: '**', redirectTo: 'dashboard', pathMatch: 'full'},
];

export const routes: ModuleWithProviders = RouterModule.forRoot(router, {useHash: true});
