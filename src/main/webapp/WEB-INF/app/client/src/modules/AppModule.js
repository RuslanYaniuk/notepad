/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define, angular) {
    "use strict";

    var dependencies = [
        "controllers/AppController",
        "controllers/NoteController",
        "controllers/NoteSideNavController",
        "controllers/SearchBoxController",
        "services/NoteService"
    ];

    define(dependencies, function (AppController, NoteController, NoteSideNavController, SearchBoxController, NoteService) {
        var moduleName = "mynote.App";

        var mod = angular.module(moduleName, [])

            .controller('AppController', AppController)
            .controller('NoteController', NoteController)
            .controller('NoteSideNavController', NoteSideNavController)
            .controller('SearchBoxController', SearchBoxController)
            .service("noteService", NoteService);

        /*mod.directive('note', function factory() {
         var directiveDefinitionObject = {
         priority: 0,
         templateUrl: 'assets/views/app.note.tpl.html',
         transclude: false,
         restrict: 'A',
         templateNamespace: 'html',
         scope: false,
         controller: AppController,
         controllerAs: 'AppController',
         bindToController: true,
         require: 'siblingDirectiveName'

         };
         return directiveDefinitionObject;
         });*/

        return moduleName
    })

})(define, angular);