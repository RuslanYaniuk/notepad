/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var NoteController = function ($scope, $mdDialog, $mdSidenav) {

            $scope.addNote = function(ev) {
                $mdDialog.show({
                    controller: NoteController,
                    templateUrl: 'assets/views/app/note.tmpl.html',
                    parent: angular.element(document.body),
                    targetEvent: ev
                });
            };

            $scope.closeAddNoteDialog = function () {
                $mdDialog.hide();
            };

            $scope.toggleSideNav = function () {
                $mdSidenav("note-side-nav")
                    .toggle();
            };

            $scope.showSearchOptionsDialog = function (ev) {
                $mdDialog.show({
                    controller: NoteController,
                    templateUrl: 'assets/views/app/search-options.dlg.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose:true
                });
            };

            $scope.closeNoteSidenav = function () {
                $mdSidenav("note-side-nav").close();
            }

        };

        return ["$scope", "$mdDialog", "$mdSidenav", NoteController];
    });

})(define);