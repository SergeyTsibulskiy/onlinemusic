'use strict';

angular.module('onlinemusicApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('album', {
                parent: 'entity',
                url: '/albums',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'onlinemusicApp.album.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/album/albums.html',
                        controller: 'AlbumController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('album');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('album.detail', {
                parent: 'entity',
                url: '/album/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'onlinemusicApp.album.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/album/album-detail.html',
                        controller: 'AlbumDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('album');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Album', function($stateParams, Album) {
                        return Album.get({id : $stateParams.id});
                    }]
                }
            })
            .state('album.new', {
                parent: 'album',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/album/album-dialog.html',
                        controller: 'AlbumDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('album', null, { reload: true });
                    }, function() {
                        $state.go('album');
                    })
                }]
            })
            .state('album.edit', {
                parent: 'album',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/album/album-dialog.html',
                        controller: 'AlbumDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Album', function(Album) {
                                return Album.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('album', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('album.delete', {
                parent: 'album',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/album/album-delete-dialog.html',
                        controller: 'AlbumDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Album', function(Album) {
                                return Album.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('album', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
