'use strict';

angular.module('onlinemusicApp').controller('MusicDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Music', 'Artist', 'Genre',
        function($scope, $stateParams, $uibModalInstance, entity, Music, Artist, Genre) {

        $scope.music = entity;
        $scope.artists = Artist.query();
        $scope.genres = Genre.query();
        $scope.load = function(id) {
            Music.get({id : id}, function(result) {
                $scope.music = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('onlinemusicApp:musicUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.music.id != null) {
                Music.update($scope.music, onSaveSuccess, onSaveError);
            } else {
                Music.save($scope.music, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
