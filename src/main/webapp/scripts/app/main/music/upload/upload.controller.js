'use strict';

angular.module('onlinemusicApp')
    .controller('UploadController', function ($scope, Principal, $http, DriveMusic) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.uploadFile = function () {
            var file = document.getElementById('file').files[0];
            // DriveMusic.uploadFile({name: file.name, file: file} , {name: file.name, file: file});

            var formData = new FormData();
            formData.append('file',file);

            $http.post('/api/drive/music/upload/',  formData, {
                headers: { 'Content-Type': undefined },
                transformRequest: angular.identity
            })
        }
    });
