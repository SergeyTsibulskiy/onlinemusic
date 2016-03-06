'use strict';

angular.module('onlinemusicApp')
    .factory('MusicSearch', function ($resource) {
        return $resource('api/_search/musics/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
