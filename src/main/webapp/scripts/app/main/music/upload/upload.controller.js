'use strict';

angular.module('onlinemusicApp')
    .controller('UploadController', function ($scope, Principal, $http, DriveMusic) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.uploadFile = function () {
            var files = document.getElementById('file').files[0];
            // DriveMusic.uploadFileToServer({files: files} , {files: files});

            var formData = new FormData();
            formData.append('file', files);

            $http.post('/api/drive/music/upload/',  formData, {
                headers: { 'Content-Type': undefined },
                transformRequest: angular.identity
            }).then(function successCallback(response) {
                alert('Success: ' + response.statusText);
            }, function errorCallback(response) {
                alert('Error: ' + response.statusText);
            });
        }
    });
