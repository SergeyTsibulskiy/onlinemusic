'use strict';

describe('Controller Tests', function() {

    describe('Artist Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockArtist, MockMusic;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockArtist = jasmine.createSpy('MockArtist');
            MockMusic = jasmine.createSpy('MockMusic');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Artist': MockArtist,
                'Music': MockMusic
            };
            createController = function() {
                $injector.get('$controller')("ArtistDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'onlinemusicApp:artistUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
