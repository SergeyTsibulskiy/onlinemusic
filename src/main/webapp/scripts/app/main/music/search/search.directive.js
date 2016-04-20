'use strict';

angular.module('onlinemusicApp')
    .directive('search', function ($timeout) {

        var controller = function ($scope) {

        };

        var link = function ($scope, $element) {

        };

        return {
            restrict: 'E',
            templateUrl: "/scripts/app/main/music/search/search.html",
            controller: controller,
            scope: {
                loadContents: '='
            },
            link: link
        };
    });
