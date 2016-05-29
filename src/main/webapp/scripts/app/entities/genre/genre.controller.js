'use strict';

angular.module('onlinemusicApp')
    .controller('GenreController', function ($scope, $state, Genre, GenreSearch, ParseLinks, Music) {
        $scope.musics = {};
        $scope.genres = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Genre.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.genres.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.genres = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            GenreSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.genres = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.loadByGenre = function (genre) {
            $scope.musics[genre] = [];
            Music.search({query: '', album: '', genre: genre }, function (result, headers) {
                _.forEach(result, function (value, key) {
                    $scope.musics[genre].push({
                        title: value.title,
                        author: value.artist.name,
                        url: value.downloadUrl,
                        duration: moment.duration(value.duration),
                        pic: value.posterUrl != null ? value.posterUrl : 'https://www.googledrive.com/host/0B8ExDrngxZU8NzJ3dTN2aTR4RXc'
                    });
                });
            })
        };

        $scope.loadByGenres = function () {

            _.each($scope.genres, function (genre) {
                $scope.loadByGenre(genre.name);
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.genre = {
                name: null,
                id: null
            };
        };
    });
