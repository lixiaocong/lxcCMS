'use strict';

angular.module('info', ['ngRoute'])
    .controller('InfoCtrl', ['$scope', '$http', function ($scope, $http) {
        $scope.password = "";
        $scope.repsword = "";

        $scope.getUser = function () {
            $http.get('/user/info').success(function (data) {
                $scope.user = data.user;
                $scope.qq = data.qq;
                $scope.facebook = data.facebook;
            })
        };

        $scope.disconnect = function (provider) {
            $http.delete('/connect/' + provider);
            location.reload();
        };

        $scope.onUpdate = function () {
            if ($scope.password != $scope.repsword)
                alert('两次输入的密码不同');
            else {
                var data = {password: $scope.password};
                $http.put('/user', data).success(function (data) {
                    if (data.result == "success") {
                        $scope.addAlert({type: "success", msg: "更新成功"});
                        $scope.getUser();
                    }
                })
            }
        };

        $scope.getUser();
    }]);
