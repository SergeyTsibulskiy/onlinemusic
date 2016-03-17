'use strict';

angular.module('onlinemusicApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('musics upload', {
                parent: 'site',
                url: '/musics/upload',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/music/upload/upload.html',
                        controller: 'UploadController'
                    }
                }
            });
    });
