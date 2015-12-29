/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var SearchBoxController = function ($scope, noteService) {

            var onSearch = function () {
                    noteService.search($scope.notesContainer.searchString);
                },

                onExitSearchMode = function () {
                    $scope.notesContainer.searchString = "";
                    noteService.getLatest()
                        .then(
                        function onSuccess_getLatest() {
                            $scope.notesContainer.searchMode = false;
                        });
                },

                onClearSearchField = function () {
                    $scope.notesContainer.searchString = '';
                    angular.element(document.getElementById('search-field')).focus();
                };

            $scope.search = onSearch;
            $scope.exitSearchMode = onExitSearchMode;
            $scope.clearSearchField = onClearSearchField;

            $scope.suggestions = [];
            $scope.notesContainer = noteService.notesContainer;
        };

        return ["$scope", "noteService", SearchBoxController];
    });

})(define);