'use strict';

angular.module('onlinemusicApp')
    .controller('ArtistController', function ($scope, $state, Artist, ArtistSearch, ParseLinks, Music) {
        $scope.musics = {};
        $scope.artists = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Artist.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.artists.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.artists = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ArtistSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.artists = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.loadByArtist = function (artist) {
            $scope.musics[artist] = [];
            Music.search({query: '', album: artist, genre: '' }, function (result, headers) {
                _.forEach(result, function (value, key) {
                    $scope.musics[artist].push({
                        title: value.title,
                        author: value.artist.name,
                        url: value.downloadUrl,
                        duration: moment.duration(value.duration),
                        pic: value.posterUrl != null ? value.posterUrl : 'https://www.googledrive.com/host/0B8ExDrngxZU8NzJ3dTN2aTR4RXc'
                    });
                });
            })
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.artist = {
                name: null,
                id: null
            };
        };
    });
