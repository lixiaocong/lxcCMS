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

angular.module('Admin', [
    'ngRoute',
    'ngAnimate',
    'ui.bootstrap',
    'dashboard',
    'info',
    'user',
    'article',
    'comment',
    'transmission',
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
    }).when('/transmission', {
        templateUrl: '/html/transmission.html',
        controller: 'TransmissionCtrl'
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