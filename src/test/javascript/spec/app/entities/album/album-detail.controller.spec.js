'use strict';

describe('Controller Tests', function() {

    describe('Album Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAlbum, MockMusic;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAlbum = jasmine.createSpy('MockAlbum');
            MockMusic = jasmine.createSpy('MockMusic');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Album': MockAlbum,
                'Music': MockMusic
            };
            createController = function() {
                $injector.get('$controller')("AlbumDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'onlinemusicApp:albumUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
