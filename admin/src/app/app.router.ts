import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { UserComponent } from './pages/user/user.component';
import { ArticleComponent } from './pages/article/article.component';
import { CommentComponent } from './pages/comment/comment.component';
import { DownloaderComponent } from './pages/downloader/downloader.component';
import { FileComponent } from './pages/file/file.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';

export const router: Routes = [
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    { path: 'user', component: UserComponent },
    { path: 'article', component: ArticleComponent },
    { path: 'comment', component: CommentComponent },
    { path: 'downloader', component: DownloaderComponent },
    { path: 'file', component: FileComponent },
    { path: 'dashboard', component: DashboardComponent},
];

export const routes: ModuleWithProviders = RouterModule.forRoot(router);
