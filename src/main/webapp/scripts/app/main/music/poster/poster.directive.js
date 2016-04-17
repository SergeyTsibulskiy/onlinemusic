'use strict';


angular.module('onlinemusicApp')
    .directive('posterMusic', function () {

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
                    var audio = document.getElementById("audio-player");

                    $("#play-button").click(function() {
                        if ($(this).hasClass("unchecked")) {
                            $(this)
                                .addClass("play-active")
                                .removeClass("play-inactive")
                                .removeClass("unchecked");
                            $(".info-two")
                                .addClass("info-active");
                            $("#pause-button")
                                .addClass("scale-animation-active");
                            $(".waves-animation-one, #pause-button, .seek-field, .volume-icon, .volume-field, .info-two").show();
                            $(".waves-animation-two").hide();
                            $("#pause-button")
                                .children('.icon')
                                .addClass("icon-pause")
                                .removeClass("icon-play");
                            setTimeout(function() {
                                $(".info-one").hide();
                            }, 400);
                            audio.play();
                            audio.currentTime = 0;
                        } else {
                            $(this)
                                .removeClass("play-active")
                                .addClass("play-inactive")
                                .addClass("unchecked");
                            $("#pause-button")
                                .children(".icon")
                                .addClass("icon-pause")
                                .removeClass("icon-play");
                            $(".info-two")
                                .removeClass("info-active");
                            $(".waves-animation-one, #pause-button, .seek-field, .volume-icon, .volume-field, .info-two").hide();
                            $(".waves-animation-two").show();
                            setTimeout(function() {
                                $(".info-one").show();
                            }, 150);
                            audio.pause();
                            audio.currentTime = 0;
                        }
                    });
                    $("#pause-button").click(function() {
                        $(this).children(".icon")
                            .toggleClass("icon-pause")
                            .toggleClass("icon-play");

                        if (audio.paused) {
                            audio.play();
                        } else {
                            audio.pause();
                        }
                    });
                    $("#play-button").click(function() {
                        setTimeout(function() {
                            $("#play-button").children(".icon")
                                .toggleClass("icon-play")
                                .toggleClass("icon-cancel");
                        }, 350);
                    });
                    $(".like").click(function() {
                        $(".icon-heart").toggleClass("like-active");
                    });

                    audio.addEventListener("timeupdate", function() {
                        var duration = document.getElementById("duration");
                        var s = parseInt(audio.currentTime % 60);
                        var m = parseInt((audio.currentTime / 60) % 60);
                        duration.innerHTML = m + ':' + s;

                        var seekbar = document.getElementById("audioSeekBar");
                        seekbar.value = audio.currentTime;
                    }, false);

                    audio.addEventListener("durationchange", function() {
                        var seekbar = document.getElementById("audioSeekBar");
                        seekbar.min = 0;
                        seekbar.max = audio.duration;
                        seekbar.value = 0;
                    });

                    document.getElementById('audioSeekBar').addEventListener('input', function () {
                        var seekbar = document.getElementById("audioSeekBar");
                        audio.currentTime = seekbar.value;
                    });

                    document.getElementById('audioVolume').addEventListener('input', function () {
                        audio.volume = this.value/100;
                    });


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
            templateUrl: "/scripts/app/main/music/poster/poster.html",
            controller: controller,
            scope: {
                loadContents: '='
            },
            link: link
        };
    });
