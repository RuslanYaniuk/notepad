/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var SearchBoxController = function ($scope, noteService) {


            $scope.suggestions = [];
            $scope.searchString = "";
            $scope.notesContainer = noteService.notesContainer;
        };

        return ["$scope", "noteService", SearchBoxController];
    });

})(define);