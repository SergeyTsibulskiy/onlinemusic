'use strict';

angular.module('onlinemusicApp')
    .factory('Album', function ($resource, DateUtils) {
        return $resource('api/albums/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
