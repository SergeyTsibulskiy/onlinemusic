'use strict';


angular.module('onlinemusicApp')
    .directive('posterMusic', function ($timeout) {

        var controller = function ($scope) {

        };

        var link = function ($scope, $element) {

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
