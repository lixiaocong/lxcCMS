'use strict';

angular.module('Admin', [
    'ngRoute',
    'ngAnimate',
    'ui.bootstrap',
    'dashboard',
    'info',
    'user',
    'article',
    'comment',
    'downloader',
    'file'
]).config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/dashboard', {
        templateUrl: '/html/dashboard.html',
        controller: 'DashboardCtrl'
    }).when('/info', {
        templateUrl: '/html/info.html',
        controller: 'InfoCtrl'
    }).when('/user', {
        templateUrl: '/html/user.html',
        controller: 'UserCtrl'
    }).when('/article', {
        templateUrl: '/html/article.html',
        controller: 'ArticleCtrl'
    }).when('/comment', {
        templateUrl: '/html/comment.html',
        controller: 'CommentCtrl'
    }).when('/downloader', {
        templateUrl: '/html/downloader.html',
        controller: 'DownloaderCtrl'
    }).when('/file', {
        templateUrl: '/html/file.html',
        controller: 'FileCtrl'
    }).otherwise({redirectTo: '/dashboard'});
}]).controller('AdminCtrl', ['$scope', function ($scope) {
    //alerts
    $scope.alerts = [];
    $scope.addAlert = function (alert) {
        $scope.alerts.push(alert);
    };
    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };

    //点击后自动折叠
    $('.nav a').on('click', function () {
        $(".navbar-collapse").collapse('hide');
    });
}]);