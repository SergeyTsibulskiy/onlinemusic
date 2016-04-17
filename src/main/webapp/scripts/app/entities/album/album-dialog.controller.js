'use strict';

angular.module('onlinemusicApp').controller('AlbumDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Album', 'Music',
        function($scope, $stateParams, $uibModalInstance, entity, Album, Music) {

        $scope.album = entity;
        $scope.musics = Music.query();
        $scope.load = function(id) {
            Album.get({id : id}, function(result) {
                $scope.album = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('onlinemusicApp:albumUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.album.id != null) {
                Album.update($scope.album, onSaveSuccess, onSaveError);
            } else {
                Album.save($scope.album, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
