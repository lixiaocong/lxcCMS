'use strict';

angular.module('file', ['ngRoute'])
    .controller('FileCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.onDelete = function (file) {
            $http.delete('/file/video?fileName=' + file).success(function (data) {
                if (data.result == 'success') {
                    $scope.getFiles();
                    $scope.addAlert({type: 'success', msg: '删除成功'});
                }
                else
                    $scope.addAlert({msg: data.message});
            })
        };

        $scope.getFiles = function () {
            $http.get('/file/video').success(function (data) {
                $scope.files = data.videos;
                $scope.serverUrl = data.serverUrl;
            })
        };

        $scope.getFiles();
    }]);
