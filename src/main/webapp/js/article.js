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

var editor = null;
function createEditor() {
    editor = null;
    editor = new wangEditor('content');
    editor.config.menuFixed = false;
    editor.config.uploadImgUrl = '/file/upload';
    editor.config.uploadImgFileName = 'imageFile';
    editor.config.menus = $.map(wangEditor.config.menus, function (item, key) {
        if (item === 'location') {
            return null;
        }
        return item;
    });
    editor.create();
}

angular.module('article', ['ngRoute'])
    .controller('ArticleCtrl', ['$scope', '$http', function ($scope, $http) {
        //editor
        setTimeout("createEditor()");

        //article
        $scope.onEdit = function (article) {
            $scope.isItemSelected = true;
            $scope.article = article;
            editor.$txt.html($scope.article.content);
        };

        $scope.onDelete = function (article) {
            $http.delete('/article/' + article.id).success(function (data) {
                if (data.result == 'success') {
                    $scope.getArticles($scope.currentPage);
                    $scope.addAlert({type: 'success', msg: '删除成功'});
                }
                else
                    $scope.addAlert({msg: data.message});
            });
        };

        $scope.onUpdate = function () {
            var data = {
                title: $scope.article.title,
                content: editor.$txt.html()
            };
            var url = '/article/' + $scope.article.id;
            $http.put(url, data).success(function (data) {
                if (data.result == 'success') {
                    $scope.isItemSelected = false;
                    $scope.getArticles($scope.currentPage);
                    $scope.addAlert({type: 'success', msg: '更新成功'});
                }
                else
                    $scope.addAlert({msg: data.message});
            });
        };

        $scope.onCancelUpdate = function () {
            $scope.isItemSelected = false;
        };

        $scope.getArticles = function (page) {
            $http.get('/article?page=' + page).success(function (data) {
                $scope.totalItems = data.articles.totalElements;
                $scope.articles = data.articles.content;
            });
        };

        //初始化文章数据
        $scope.isItemSelected = false;
        $scope.currentPage = 1;
        $scope.getArticles($scope.currentPage);
    }]);