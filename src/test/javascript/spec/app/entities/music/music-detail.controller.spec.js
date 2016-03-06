'use strict';

describe('Controller Tests', function() {

    describe('Music Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMusic, MockArtist, MockGenre;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMusic = jasmine.createSpy('MockMusic');
            MockArtist = jasmine.createSpy('MockArtist');
            MockGenre = jasmine.createSpy('MockGenre');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Music': MockMusic,
                'Artist': MockArtist,
                'Genre': MockGenre
            };
            createController = function() {
                $injector.get('$controller')("MusicDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'onlinemusicApp:musicUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
