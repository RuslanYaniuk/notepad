/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define, angular) {
    "use strict";

    define([
            "controllers/NoteController",
            "controllers/NoteSideNavController",
            "controllers/NotesListController",
            "controllers/NoteSearchController",
            "controllers/MainToolbarController",
            "services/Notes",
            "services/DispatcherService",
            "services/MainToolBar",
            "directives/SearchBox",
            "directives/NotesList",
            "directives/OnScroll"
        ],
        function (NoteController,
                  NoteSideNavController,
                  NotesListController,
                  NoteSearchController,
                  MainToolbarController,
                  Notes,
                  DispatcherService,
                  MainToolBar,
                  SearchBoxDirective,
                  NotesListDirective,
                  OnScrollDirective) {
            var moduleName = "mynote.App";

            angular.module(moduleName, [])

                .controller('NoteController', NoteController)
                .controller('NoteSideNavController', NoteSideNavController)
                .controller('NotesListController', NotesListController)
                .controller('NoteSearchController', NoteSearchController)
                .controller('MainToolbarController', MainToolbarController)

                .service('notes', Notes)
                .service('dispatcherService', DispatcherService)
                .service('mainToolBar', MainToolBar)

                .directive('searchbox', SearchBoxDirective)
                .directive('notesList', NotesListDirective)
                .directive('onScroll', OnScrollDirective);

            return moduleName
        })

})(define, angular);