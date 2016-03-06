'use strict';

describe('Controller Tests', function() {

    describe('Genre Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockGenre, MockMusic;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockGenre = jasmine.createSpy('MockGenre');
            MockMusic = jasmine.createSpy('MockMusic');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Genre': MockGenre,
                'Music': MockMusic
            };
            createController = function() {
                $injector.get('$controller')("GenreDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'onlinemusicApp:genreUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
