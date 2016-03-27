'use strict';
var ap;
angular.module('onlinemusicApp')
    .directive('aplayer', function() {

        var controller = function ($scope) {
            if ($scope.loadContents) {
                $scope.loadContents().$promise.then(function (response) {
                    $scope.musics = [];
                    _.forEach(response, function (value, key) {
                        $scope.musics.push({
                            title: value.title,
                            author: value.artist.name,
                            url: value.downloadUrl,
                            pic: value.posterUrl
                        });
                    });

                    $scope.isLoaded = true;
                })
            }

            $scope.initPlayer = function ($scope) {
                if ($scope.musics.length != 0) {
                    ap = new APlayer({
                        element: document.getElementById('player1'),
                        narrow: false,
                        autoplay: false,
                        showlrc: false,
                        theme: '#e6d0b2',
                        music: $scope.musics
                    });
                    ap.init();
                } else {
                    console.log("Song not found");
                }
            }

        };

        var link = function ($scope) {
            $scope.$watch('isLoaded', function (newVal) {
                if ($scope.isLoaded) {
                    $scope.initPlayer($scope);
                }
            });
        };

        return {
            restrict: 'E',
            templateUrl: "/scripts/app/main/music/player/player.html",
            controller: controller,
            scope: {
                loadContents: '='
            },
            link: link
        };
    });
