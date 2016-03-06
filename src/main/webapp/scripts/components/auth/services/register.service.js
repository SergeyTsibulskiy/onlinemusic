'use strict';

angular.module('onlinemusicApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


