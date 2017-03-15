'use strict';

angular.module('dashboard', ['ngRoute'])
    .controller('DashboardCtrl', ['$scope', '$http', function ($scope, $http) {
        $http.get('/user').success(function (data) {
            $scope.userNumber = data.users.totalElements;
        });

        $http.get('/article').success(function (data) {
            $scope.articleNumber = data.articles.totalElements;
        });

        $http.get('/comment').success(function (data) {
            $scope.commentNumber = data.comments.totalElements;
        });
    }]);