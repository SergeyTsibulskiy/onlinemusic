'use strict';

angular.module('onlinemusicApp').factory('DriveMusic', function ($resource) {
    var URL = '/api/drive/music';

    return $resource(URL, {}, {
        uploadFile: {
            method: 'POST',
            url: URL + '/upload',
            headers: {'Content-Type': undefined },
            transformRequest: function (data) {
                var formData = new FormData();

                formData.append('name', new Blob([angular.toJson(data.name)], {
                    type: "application/json"
                }));
                formData.append("file", data);
                return formData;
            },
            data: {}
        }
    });
});
