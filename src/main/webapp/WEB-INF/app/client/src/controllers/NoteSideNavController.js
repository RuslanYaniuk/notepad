/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var NoteSideNavController = function ($scope, $mdDialog, $mdSidenav) {

            var onShowSearchOptionsDialog = function (ev) {
                    $mdDialog.show({
                        controller: 'NoteSearchController',
                        templateUrl: 'assets/views/app/note.search-options.dlg.html',
                        parent: angular.element(document.body),
                        targetEvent: ev,
                        clickOutsideToClose: true
                    });
                },

                onCloseSideNav = function () {
                    $mdSidenav("note-side-nav").close();
                };

            $scope.showSearchOptionsDialog = onShowSearchOptionsDialog;
            $scope.closeSideNav = onCloseSideNav;
        };

        return [
            "$scope",
            "$mdDialog",
            "$mdSidenav", NoteSideNavController];
    });

})(define);