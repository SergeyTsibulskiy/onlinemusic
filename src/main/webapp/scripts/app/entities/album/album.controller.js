'use strict';

angular.module('onlinemusicApp')
    .controller('AlbumController', function ($scope, $state, Album, ParseLinks, Music) {
        $scope.musics = {};
        $scope.albums = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Album.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.albums.push(result[i]);
                }
            });
        };

        $scope.loadByAlbum = function (album) {
            $scope.musics[album] = [];
            Music.search({query: '', album: album, genre: '' }, function (result, headers) {
                _.forEach(result, function (value, key) {
                    $scope.musics[album].push({
                        title: value.title,
                        author: value.artist.name,
                        url: value.downloadUrl,
                        duration: moment.duration(value.duration),
                        pic: value.posterUrl != null ? value.posterUrl : 'https://www.googledrive.com/host/0B8ExDrngxZU8NzJ3dTN2aTR4RXc'
                    });
                });
            })
        };

        $scope.reset = function() {
            $scope.page = 0;
            $scope.albums = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.album = {
                name: null,
                id: null
            };
        };
    });
