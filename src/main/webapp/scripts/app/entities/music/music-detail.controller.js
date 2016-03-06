'use strict';

angular.module('onlinemusicApp')
    .controller('MusicDetailController', function ($scope, $rootScope, $stateParams, entity, Music, Artist, Genre) {
        $scope.music = entity;
        $scope.load = function (id) {
            Music.get({id: id}, function(result) {
                $scope.music = result;
            });
        };
        var unsubscribe = $rootScope.$on('onlinemusicApp:musicUpdate', function(event, result) {
            $scope.music = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
