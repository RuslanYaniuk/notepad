/**
 * @author Ruslan Yaniuk
 * @date July 2015
 */
(function (define) {
    "use strict";

    define(function () {
        var AppController = function ($scope, $compile) {
            /* var directive = ("<div note class='note-template'></div>");

             $scope.addNew = function($event) {
             var saveButton = $($event.target),
             note = saveButton.closest('.row');

             var noteTemplate = $($compile(directive)($scope));

             noteTemplate.insertBefore(note);
             noteTemplate.addClass("note-row-animation");
             noteTemplate.css("{display: block; margin-top: 0}");

             saveButton.remove();
             }*/
        };

        return ["$scope", "$compile", AppController];
    });

})(define);