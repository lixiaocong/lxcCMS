import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { UserComponent } from './pages/user/user.component';
import { ArticleComponent } from './pages/article/article.component';
import { CommentComponent } from './pages/comment/comment.component';
import { DownloaderComponent } from './pages/downloader/downloader.component';
import { FileComponent } from './pages/file/file.component';

export const router: Routes = [
    { path: '', redirectTo: 'downloader', pathMatch: 'full' },
    { path: 'user', component: UserComponent },
    { path: 'article', component: ArticleComponent },
    { path: 'comment', component: CommentComponent },
    { path: 'downloader', component: DownloaderComponent },
    { path: 'file', component: FileComponent },
];

export const routes: ModuleWithProviders = RouterModule.forRoot(router);
