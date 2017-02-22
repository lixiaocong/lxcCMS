/*
 BSD 3-Clause License

 Copyright (c) 2016, lixiaocong(lxccs@iCloud.com)
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of the copyright holder nor the names of its
 contributors may be used to endorse or promote products derived from
 this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

'use strict';

angular.module('transmission', ['ngRoute'])
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
    }]).filter("transmissionStatus", function () {
    return function (input) {
        switch (input) {
            case 0:
                return "停止";
            case 1:
                return "等待检查文件";
            case 2:
                return "检查文件";
            case 3:
                return "等待下载";
            case 4:
                return "下载中";
            case 5:
                return "等待做种";
            case 6:
                return "做种中";
            case 7:
                return "找不到可用节点";
            default:
                return "未知状态!!!";
        }
    }
}).filter("bytes", function () {
    return function (bytes, precision) {
        if (isNaN(parseFloat(bytes)) || !isFinite(bytes)) return '-';
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
    .controller('TransmissionCtrl', ['$scope', '$uibModal', '$http', '$interval', function ($scope, $uibModal, $http, $interval) {
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
            $scope.torrents.forEach(function (torrent) {
                if (torrent.isChecked) {
                    $http.put('/transmission/start?id=' + torrent.id, null).success(function (data) {
                        if (data.result != 'success')
                            $scope.addAlert({type: 'success', msg: '启动失败'});
                    })
                }
            })
        };

        $scope.stop = function () {
            $scope.torrents.forEach(function (torrent) {
                if (torrent.isChecked) {
                    $http.put('/transmission/stop?id=' + torrent.id, null).success(function (data) {
                        if (data.result != 'success')
                            $scope.addAlert({type: 'success', msg: '暂停失败'});
                    })
                }
            })
        };

        $scope.delete = function () {
            $scope.torrents.forEach(function (torrent) {
                if (torrent.isChecked) {
                    $http.delete('/transmission?id=' + torrent.id).success(function (data) {
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
            $http.get('/transmission').success(function (data) {
                if (data.result == 'success') {
                    for (var i = 0; i < $scope.torrents.length; i++) {
                        var find = false;
                        var oldTorrent = $scope.torrents[i];
                        for (var j = 0; j < data.torrents.length; j++) {
                            var newTorrent = data.torrents[j];
                            if (oldTorrent.id == newTorrent.id) {
                                //update the existing item and mark the data,break loop
                                oldTorrent.name = newTorrent.name;
                                oldTorrent.percentDone = newTorrent.percentDone;
                                oldTorrent.status = newTorrent.status;
                                oldTorrent.totalSize = newTorrent.totalSize;
                                oldTorrent.downloadedEver = newTorrent.downloadedEver;
                                oldTorrent.rateDownload = newTorrent.rateDownload;
                                data.torrents.splice(j--, 1);
                                find = true;
                                break;
                            }
                        }
                        if (!find)
                            $scope.torrents.splice(i--, 1);//delete the deleted item
                    }
                    //add unmarked items
                    data.torrents.forEach(function (newTorrent) {
                        newTorrent.isChecked = false;
                        $scope.torrents.push(newTorrent);
                    })
                }
            });
        };

        $scope.torrents = [];
        $scope.getTorrents();

        var timer = $interval($scope.getTorrents, 2000);
        $scope.$on('$destroy', function () {
            if (timer)
                $interval.cancel(timer);
        });

    }])
    .controller('chooseFileCtrl', function ($scope, fileUpload, $uibModalInstance, callback) {
        $scope.ok = function () {
            fileUpload.uploadFileToUrl($scope.file, '/transmission', callback);
            $uibModalInstance.close();
        };

        $scope.cancel = function () {
            $uibModalInstance.dismiss();
        };
    });