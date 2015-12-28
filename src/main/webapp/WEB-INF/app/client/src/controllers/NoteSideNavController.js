/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var NoteSideNavController = function ($scope, $mdDialog, $mdSidenav) {

            var onToggleSideNav = function () {
                    $mdSidenav("note-side-nav")
                        .toggle();
                },

                onShowSearchOptionsDialog = function (ev) {
                    $mdDialog.show({
                        controller: NoteSideNavController,
                        templateUrl: 'assets/views/app/search-options.dlg.html',
                        parent: angular.element(document.body),
                        targetEvent: ev,
                        clickOutsideToClose: true
                    });
                },

                onCloseNoteSideNav = function () {
                    $mdSidenav("note-side-nav").close();
                };

            $scope.toggleSideNav = onToggleSideNav;
            $scope.showSearchOptionsDialog = onShowSearchOptionsDialog;
            $scope.closeNoteSideNav = onCloseNoteSideNav;
        };

        return ["$scope", "$mdDialog", "$mdSidenav", NoteSideNavController];
    });

})(define);