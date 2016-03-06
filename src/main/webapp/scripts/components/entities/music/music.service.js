'use strict';

angular.module('onlinemusicApp')
    .factory('Music', function ($resource, DateUtils) {
        return $resource('api/musics/:id', {}, {
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
