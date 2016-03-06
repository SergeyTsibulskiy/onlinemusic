'use strict';

angular.module('onlinemusicApp')
    .controller('ArtistDetailController', function ($scope, $rootScope, $stateParams, entity, Artist, Music) {
        $scope.artist = entity;
        $scope.load = function (id) {
            Artist.get({id: id}, function(result) {
                $scope.artist = result;
            });
        };
        var unsubscribe = $rootScope.$on('onlinemusicApp:artistUpdate', function(event, result) {
            $scope.artist = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
