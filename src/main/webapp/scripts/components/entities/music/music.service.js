'use strict';

angular.module('onlinemusicApp')
    .factory('Music', function ($resource, DateUtils) {
        return $resource('api/musics/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': {method: 'PUT'},
            'search': {
                // url: 'api/musics/search?query=:query&album=:album&genre=:genre',
                url: 'api/musics/search',
                method: 'POST',
                isArray: true,
                params: {
                    query: '@query',
                    album: '@album',
                    genre: '@genre'
                }
            }
        });
    });
