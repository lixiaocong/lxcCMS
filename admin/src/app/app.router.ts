import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { UserComponent } from './user/user.component';
import { ArticleComponent } from './article/article.component';
import { CommentComponent } from './comment/comment.component';
import { DownloaderComponent } from './downloader/downloader.component';
import { FileComponent } from './file/file.component';

export const router: Routes = [
    { path: '', redirectTo: 'user', pathMatch: 'full' },
    { path: 'user', component: UserComponent },
    { path: 'article', component: ArticleComponent },
    { path: 'comment', component: CommentComponent },
    { path: 'downloader', component: DownloaderComponent },
    { path: 'file', component: FileComponent },
];

export const routes: ModuleWithProviders = RouterModule.forRoot(router);