/**
 * @author Ruslan Yaniuk
 * @date June 2016
 */
(function (define) {
    "use strict";

    define(function () {

        var NoteSearchController = function ($scope, $mdDialog, notes) {
            $scope.query = notes.query;
            $scope.findNotes = notes.findNotes;
            $scope.mdDialog = $mdDialog;
        };

        return ["$scope", "$mdDialog", "notes", NoteSearchController];
    });

})(define);