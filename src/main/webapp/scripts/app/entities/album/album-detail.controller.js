'use strict';

angular.module('onlinemusicApp')
    .controller('AlbumDetailController', function ($scope, $rootScope, $stateParams, entity, Album, Music) {
        $scope.album = entity;
        $scope.load = function (id) {
            Album.get({id: id}, function(result) {
                $scope.album = result;
            });
        };
        var unsubscribe = $rootScope.$on('onlinemusicApp:albumUpdate', function(event, result) {
            $scope.album = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
