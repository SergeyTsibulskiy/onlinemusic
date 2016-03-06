'use strict';

angular.module('onlinemusicApp')
    .factory('GenreSearch', function ($resource) {
        return $resource('api/_search/genres/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
