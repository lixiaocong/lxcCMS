'use strict';

angular.module('comment', ['ngRoute'])
    .controller('CommentCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.onEdit = function (comment) {
            $scope.isItemSelected = true;
            $scope.comment = comment;
        };

        $scope.onDelete = function (comment) {
            $http.delete('/comment/' + comment.id).success(function (data) {
                if (data.result == 'success') {
                    $scope.getComments($scope.currentPage);
                    $scope.addAlert({type: 'success', msg: '删除成功'});
                }
                else
                    $scope.addAlert({msg: data.message});
            })
        };

        $scope.onUpdate = function () {
            var data = {content: $scope.comment.content};
            $http.put('/comment/' + $scope.comment.id, data).success(function (data) {
                if (data.result == 'success') {
                    $scope.isItemSelected = false;
                    $scope.getComments($scope.currentPage);
                    $scope.addAlert({type: 'success', msg: '更新成功'});
                }
                else
                    $scope.addAlert({msg: data.message});
            })
        };

        $scope.onCancelUpdate = function () {
            $scope.isItemSelected = false;
        };

        $scope.getComments = function (page) {
            $http.get('/comment?page=' + page).success(function (data) {
                $scope.totalItems = data.comments.totalElements;
                $scope.comments = data.comments.content;
            })
        };

        $scope.isItemSelected = false;
        $scope.currentPage = 1;
        $scope.getComments($scope.currentPage);
    }]);