'use strict';

angular.module('onlinemusicApp')
	.controller('MusicDeleteController', function($scope, $uibModalInstance, entity, Music) {

        $scope.music = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Music.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
