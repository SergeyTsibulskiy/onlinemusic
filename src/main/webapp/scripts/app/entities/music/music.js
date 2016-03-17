'use strict';

angular.module('onlinemusicApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('music', {
                parent: 'entity',
                url: '/musics',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'onlinemusicApp.music.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/music/musics.html',
                        controller: 'MusicController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('music');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('music.detail', {
                parent: 'entity',
                url: '/music/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'onlinemusicApp.music.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/music/music-detail.html',
                        controller: 'MusicDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('music');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Music', function($stateParams, Music) {
                        return Music.get({id : $stateParams.id});
                    }]
                }
            })
            .state('music.new', {
                parent: 'music',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/music/music-dialog.html',
                        controller: 'MusicDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    head: null,
                                    title: null,
                                    album: null,
                                    year: null,
                                    comment: null,
                                    cloudId: null,
                                    posterUrl: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('music', null, { reload: true });
                    }, function() {
                        $state.go('music');
                    })
                }]
            })
            .state('music.edit', {
                parent: 'music',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/music/music-dialog.html',
                        controller: 'MusicDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Music', function(Music) {
                                return Music.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('music', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('music.delete', {
                parent: 'music',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/music/music-delete-dialog.html',
                        controller: 'MusicDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Music', function(Music) {
                                return Music.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('music', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
