'use strict';

angular.module('onlinemusicApp').controller('GenreDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Genre', 'Music',
        function($scope, $stateParams, $uibModalInstance, entity, Genre, Music) {

        $scope.genre = entity;
        $scope.musics = Music.query();
        $scope.load = function(id) {
            Genre.get({id : id}, function(result) {
                $scope.genre = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('onlinemusicApp:genreUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.genre.id != null) {
                Genre.update($scope.genre, onSaveSuccess, onSaveError);
            } else {
                Genre.save($scope.genre, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
