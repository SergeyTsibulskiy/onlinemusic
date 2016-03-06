 'use strict';

angular.module('onlinemusicApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-onlinemusicApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-onlinemusicApp-params')});
                }
                return response;
            }
        };
    });
