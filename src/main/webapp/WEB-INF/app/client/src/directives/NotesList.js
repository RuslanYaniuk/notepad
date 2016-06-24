/**
 * @author Ruslan Yaniuk
 * @date June 2016
 */
(function (define) {
    "use strict";

    define(function () {

        var NotesList = function factory() {
            return {
                priority: 0,
                templateUrl: 'assets/views/directives/notes-list.html',
                transclude: false,
                restrict: 'A',
                scope: false,
                controllerAs: 'NoteController',
                bindToController: true
            };
        };

        return NotesList;
    });

})(define);