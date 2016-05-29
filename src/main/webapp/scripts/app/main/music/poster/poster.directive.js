'use strict';


angular.module('onlinemusicApp')
    .directive('posterMusic', function () {

        var init = function () {
            setTimeout(function () {
                var audio = $(".audio-player");

                $(".play-button").click(function () {
                    var $parent = $(this).closest('.poster-wrapper');
                    var audio = $parent.find('.audio-player').get(0);
                    if ($(this).hasClass("unchecked")) {
                        $(this)
                            .addClass("play-active")
                            .removeClass("play-inactive")
                            .removeClass("unchecked");
                        $parent.find(".info-two")
                            .addClass("info-active");
                        $parent.find(".pause-button")
                            .addClass("scale-animation-active");
                        $parent.find(".waves-animation-one, .pause-button, .seek-field, .volume-icon, .volume-field, .info-two").show();
                        $parent.find(".waves-animation-two").hide();
                        $parent.find(".pause-button")
                            .children('.icon')
                            .addClass("icon-pause")
                            .removeClass("icon-play");
                        setTimeout(function () {
                            $parent.find(".info-one").hide();
                        }, 400);

                        audio.play();
                        audio.currentTime = 0;
                    } else {
                        $(this)
                            .removeClass("play-active")
                            .addClass("play-inactive")
                            .addClass("unchecked");
                        $parent.find(".pause-button")
                            .children(".icon")
                            .addClass("icon-pause")
                            .removeClass("icon-play");
                        $parent.find(".info-two")
                            .removeClass("info-active");
                        $parent.find(".waves-animation-one, .pause-button, .seek-field, .volume-icon, .volume-field, .info-two").hide();
                        $parent.find(".waves-animation-two").show();
                        setTimeout(function () {
                            $(".info-one").show();
                        }, 150);
                        audio.pause();
                        audio.currentTime = 0;
                    }
                });
                $(".pause-button").click(function () {
                    var $parent = $(this).closest('.poster-wrapper');
                    var audio = $parent.find('.audio-player').get(0);
                    $(this).children(".icon")
                        .toggleClass("icon-pause")
                        .toggleClass("icon-play");

                    if (audio.paused) {
                        audio.play();
                    } else {
                        audio.pause();
                    }
                });
                $(".play-button").click(function () {
                    var $btn = $(this);
                    setTimeout(function () {
                        $btn.children(".icon")
                            .toggleClass("icon-play")
                            .toggleClass("icon-cancel");
                    }, 350);
                });
                $(".like").click(function () {
                    var $parent = $(this).closest('.poster-wrapper');
                    $parent.find(".icon-heart").toggleClass("like-active");
                });

                audio.on('timeupdate', function () {
                    var $parent = $(this).closest('.poster-wrapper');
                    var audio = $parent.find('.audio-player').get(0);
                    var duration = $parent.find(".duration");
                    var s = parseInt($(this).get(0).currentTime % 60);
                    var m = parseInt(($(this).get(0).currentTime / 60) % 60);
                    duration.text(m + ':' + s);

                    var seekbar = $parent.find(".audioSeekBar");
                    seekbar.value = audio.currentTime;
                });

                audio.on('durationchange', function () {
                    var $parent = $(this).closest('.poster-wrapper');

                    var audio = $parent.find('.audio-player').get(0);
                    var seekbar = $parent.find(".audioSeekBar");
                    seekbar.attr('min', 0);
                    seekbar.attr('max', audio.duration);
                    seekbar.val(0);
                });

                $('.audioSeekBar').on('input', function () {
                    var $parent = $(this).closest('.poster-wrapper');
                    var seekbar = $parent.find(".audioSeekBar");
                    $parent.find('.audio-player').get(0).currentTime = seekbar.val();
                });


                $('.audioVolume').on('input', function () {
                    var $parent = $(this).closest('.poster-wrapper');
                    $parent.find('.audio-player').get(0).volume = $(this).val() / 100;
                });

            }, 500);

        };

        var controller = function ($scope) {
            $scope.update = function () {
                // $scope.musics = dataService.musics;
            }
        };

        var link = function($scope, element, attr) {
            $scope.$watch('musics', function(newValue, oldValue) {
                init();
            });
            console.log(1);
        };

        return {
            restrict: 'E',
            templateUrl: "/scripts/app/main/music/poster/poster.html",
            controller: controller,
            scope: {
                musics: '='
            },
            link: link
        };
    });
