'use strict';

angular.module('onlinemusicApp')
    .controller('SearchController', function ($scope, $state, Music, ParseLinks) {

        // $scope.musics = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;

        Music.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
            $scope.links = ParseLinks.parse(headers('link'));
            $scope.musics = [];
            _.forEach(result, function (value, key) {
                $scope.musics.push({
                    title: value.title,
                    author: value.artist.name,
                    url: value.downloadUrl,
                    duration: moment.duration(value.duration),
                    pic: value.posterUrl != null ? value.posterUrl : 'https://www.googledrive.com/host/0B8ExDrngxZU8NzJ3dTN2aTR4RXc'
                });
            });
            // angular.copy($scope.musics, dataService.musics);
        });

    });
    // .factory('dataService', [function () {
    //     return {musics: []};
    // }]);

