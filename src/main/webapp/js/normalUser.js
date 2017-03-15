'use strict';

angular.module('Admin', [
    'ngRoute',
    'ngAnimate',
    'ui.bootstrap',
    'info',
    'article',
    'comment'
]).config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/info', {
        templateUrl: '/html/info.html',
        controller: 'InfoCtrl'
    }).when('/article', {
        templateUrl: '/html/article.html',
        controller: 'ArticleCtrl'
    }).when('/comment', {
        templateUrl: '/html/comment.html',
        controller: 'CommentCtrl'
    }).otherwise({redirectTo: '/info'});
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