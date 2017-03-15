'use strict';

angular.module('downloader', ['ngRoute'])
    .directive('fileModel', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;

                element.bind('change', function () {
                    scope.$apply(function () {
                        modelSetter(scope, element[0].files[0]);
                    });
                });
            }
        };
    }]).filter("bytes", function () {
    return function (bytes, precision) {
        if(bytes==0)
            return '-';
        if (typeof precision === 'undefined') precision = 1;
        var units = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'],
            number = Math.floor(Math.log(bytes) / Math.log(1024));
        return (bytes / Math.pow(1024, Math.floor(number))).toFixed(precision) + ' ' + units[number];
    }
}).service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function (file, uploadUrl, callback) {
        var fd = new FormData();
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        }).success(function () {
            callback(true);
        }).error(function () {
            callback(false);
        });
    }
}])
    .controller('DownloaderCtrl', ['$scope', '$uibModal', '$http', '$interval', function ($scope, $uibModal, $http, $interval) {
        $scope.onAddSuccess = function (result) {
            if (!result)
                $scope.addAlert({type: 'success', msg: '添加失败'});
        };

        $scope.open = function () {
            $uibModal.open({
                animation: true,
                templateUrl: 'chooseFile.html',
                controller: 'chooseFileCtrl',
                size: 'lg',
                resolve: {
                    callback: function () {
                        return $scope.onAddSuccess;
                    }
                }
            });
        };

        $scope.start = function () {
            $scope.tasks.forEach(function (task) {
                if (task.isChecked) {
                    $http.put('/downloader/start?id=' + task.id, null).success(function (data) {
                        if (data.result != 'success')
                            $scope.addAlert({type: 'success', msg: '启动失败'});
                    })
                }
            })
        };

        $scope.stop = function () {
            $scope.tasks.forEach(function (task) {
                if (task.isChecked) {
                    $http.put('/downloader/stop?id=' + task.id, null).success(function (data) {
                        if (data.result != 'success')
                            $scope.addAlert({type: 'success', msg: '暂停失败'});
                    })
                }
            })
        };

        $scope.delete = function () {
            $scope.tasks.forEach(function (task) {
                if (task.isChecked) {
                    $http.delete('/downloader?id=' + task.id).success(function (data) {
                        if (data.result != 'success')
                            $scope.addAlert({type: 'success', msg: '删除失败'});
                    })
                }
            })
        };

        $scope.onCheck = function (torrent) {
            torrent.isChecked = !torrent.isChecked;
        };

        $scope.getTorrents = function () {
            $http.get('/downloader').success(function (data) {
                if (data.result == 'success') {
                    for (var i = 0; i < $scope.tasks.length; i++) {
                        var find = false;
                        var oldTorrent = $scope.tasks[i];
                        for (var j = 0; j < data.tasks.length; j++) {
                            var newTorrent = data.tasks[j];
                            if (oldTorrent.id == newTorrent.id) {
                                //update the existing item and mark the data,break loop
                                oldTorrent.downloadType= newTorrent.downloadType;
                                oldTorrent.name = newTorrent.name;
                                oldTorrent.totalLength= newTorrent.totalLength;
                                oldTorrent.downloadedLength= newTorrent.downloadedLength;
                                oldTorrent.speed= newTorrent.speed;
                                oldTorrent.status= newTorrent.status;
                                oldTorrent.finished= newTorrent.finished;
                                data.tasks.splice(j--, 1);
                                find = true;
                                break;
                            }
                        }
                        if (!find)
                            $scope.tasks.splice(i--, 1);//delete the deleted item
                    }
                    //add unmarked items
                    data.tasks.forEach(function (newTorrent) {
                        newTorrent.isChecked = false;
                        $scope.tasks.push(newTorrent);
                    })
                }
            });
        };

        $scope.tasks = [];
        $scope.getTorrents();

        var timer = $interval($scope.getTorrents, 2000);
        $scope.$on('$destroy', function () {
            if (timer)
                $interval.cancel(timer);
        });

    }])
    .controller('chooseFileCtrl', function ($scope, fileUpload, $uibModalInstance, callback) {
        $scope.ok = function () {
            fileUpload.uploadFileToUrl($scope.file, '/downloader', callback);
            $uibModalInstance.close();
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss();
        };
    });