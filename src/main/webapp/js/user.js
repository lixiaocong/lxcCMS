'use strict';

angular.module('user', ['ngRoute'])
    .controller('UserCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.onDelete = function (user) {
            $http.delete('/user/' + user.id).success(function (data) {
                if (data.result == 'success') {
                    $scope.getUsers($scope.currentPage);
                    $scope.addAlert({type: 'success', msg: '删除成功'});
                }
            })
        };

        $scope.getUsers = function (page) {
            $http.get('/user?page=' + page).success(function (data) {
                $scope.totalItems = data.users.totalElements;
                $scope.users = data.users.content;
            })
        };

        $scope.currentPage = 1;
        $scope.getUsers($scope.currentPage);
    }]);