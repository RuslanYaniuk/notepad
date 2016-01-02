/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define, angular) {
    "use strict";

    define([
            "controllers/NoteController",
            "controllers/NoteSideNavController",
            "controllers/SearchBoxController",
            "services/NoteService",
            "services/DispatcherService"
        ],
        function (NoteController,
                  NoteSideNavController,
                  SearchBoxController,
                  NoteService,
                  DispatcherService) {
            var moduleName = "mynote.App";

            angular.module(moduleName, [])

                .controller('NoteController', NoteController)
                .controller('NoteSideNavController', NoteSideNavController)
                .controller('SearchBoxController', SearchBoxController)
                .service('noteService', NoteService)
                .service('dispatcherService', DispatcherService)

                .directive('searchbox', function factory() {
                    return {
                        priority: 0,
                        templateUrl: 'assets/views/directives/search-box.html',
                        transclude: false,
                        restrict: 'A',
                        scope: false,
                        controller: SearchBoxController,
                        controllerAs: 'SearchBoxController',
                        bindToController: true
                    };
                });

            return moduleName
        })

})(define, angular);