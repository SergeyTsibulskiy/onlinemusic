'use strict';

angular.module('onlinemusicApp').controller('ArtistDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Artist', 'Music',
        function($scope, $stateParams, $uibModalInstance, entity, Artist, Music) {

        $scope.artist = entity;
        $scope.musics = Music.query();
        $scope.load = function(id) {
            Artist.get({id : id}, function(result) {
                $scope.artist = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('onlinemusicApp:artistUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.artist.id != null) {
                Artist.update($scope.artist, onSaveSuccess, onSaveError);
            } else {
                Artist.save($scope.artist, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
