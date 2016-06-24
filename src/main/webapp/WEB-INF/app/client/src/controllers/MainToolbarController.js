/**
 * @author Ruslan Yaniuk
 * @date June 2016
 */
(function (define) {
    "use strict";

    define(function () {

        var MainToolbarController = function ($scope, $mdSidenav, notes, mainToolBar) {

            var onToggleSideNav = function () {
                $mdSidenav("note-side-nav").toggle();
            };

            $scope.toggleSideNav = onToggleSideNav;
            $scope.state = mainToolBar.state;
            $scope.query = notes.query;
        };

        return [
            "$scope",
            "$mdSidenav",
            "notes",
            "mainToolBar", MainToolbarController];
    });

})(define);
