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
                        controllerAs: 'NoteController',
                        bindToController: true
                    };
                })

                .directive('notesList', function factory() {
                    return {
                        priority: 0,
                        templateUrl: 'assets/views/directives/notes-list.html',
                        transclude: false,
                        restrict: 'A',
                        scope: false,
                        controllerAs: 'NoteController',
                        bindToController: true
                    };
                })

                .directive('onScrollEnd', function () {
                    return {
                        restrict: 'A',
                        link: function (scope, element, attrs) {
                            element.bind('scroll', function () {
                                var target = element[0],
                                    leftToScroll =
                                        target.scrollHeight - target.scrollTop - target.clientHeight;

                                if (leftToScroll < 50) {
                                    scope.$apply(function (self) {
                                        self[attrs.onScrollEnd](element[0]);
                                    });
                                }
                            });
                        }
                    };
                });

            return moduleName
        })

})(define, angular);