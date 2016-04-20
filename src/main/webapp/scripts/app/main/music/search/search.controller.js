'use strict';

angular.module('onlinemusicApp')
    .controller('SearchController', function ($scope, $state, Music, ParseLinks) {

        $scope.musics = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
    });
