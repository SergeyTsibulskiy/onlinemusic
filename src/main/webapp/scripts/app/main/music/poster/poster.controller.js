'use strict';

angular.module('onlinemusicApp')
    .controller('PosterController', function ($scope, $state, Music, ParseLinks) {

        $scope.musics = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            return Music.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}).$promise.then(function (response) {
                $scope.musics = [];
                _.forEach(response, function (value, key) {
                    $scope.musics.push({
                        title: value.title,
                        author: value.artist.name,
                        url: value.downloadUrl,
                        duration: moment.duration(value.duration),
                        pic: value.posterUrl != null ? value.posterUrl : 'https://www.googledrive.com/host/0B8ExDrngxZU8NzJ3dTN2aTR4RXc'
                    });
                });
            })
        };
    });
