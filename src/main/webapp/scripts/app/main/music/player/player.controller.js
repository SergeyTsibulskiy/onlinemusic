'use strict';

angular.module('onlinemusicApp')
    .controller('PlayerController', function ($scope, $state, Music, ParseLinks) {

        $scope.musics = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            return Music.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']});
        };

    });
