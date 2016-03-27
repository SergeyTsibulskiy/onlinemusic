'use strict';

angular.module('onlinemusicApp').factory('DriveMusic', function ($resource) {
    var URL = '/api/drive/music';

    return $resource(URL, {}, {
        uploadFileToServer: {
            method: 'POST',
            url: URL + '/uploads',
            headers: {'Content-Type': undefined },
            isArray: true
        }
    });
});
