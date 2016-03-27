'use strict';

angular.module('onlinemusicApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('my music', {
                parent: 'site',
                url: '/mymusics',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/music/main/main.html',
                        controller: 'MusicController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        return $translate.refresh();
                    }]
                }
            });
    });
