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