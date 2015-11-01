/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define, angular) {
    "use strict";

    var dependencies = [
        "controllers/AppController"
    ];

    define(dependencies, function (AppController) {
        var moduleName = "mynote.App";

        var mod = angular.module(moduleName, [])

            .controller('AppController', AppController);

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