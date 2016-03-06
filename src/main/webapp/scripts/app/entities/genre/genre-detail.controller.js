'use strict';

angular.module('onlinemusicApp')
    .controller('GenreDetailController', function ($scope, $rootScope, $stateParams, entity, Genre, Music) {
        $scope.genre = entity;
        $scope.load = function (id) {
            Genre.get({id: id}, function(result) {
                $scope.genre = result;
            });
        };
        var unsubscribe = $rootScope.$on('onlinemusicApp:genreUpdate', function(event, result) {
            $scope.genre = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
