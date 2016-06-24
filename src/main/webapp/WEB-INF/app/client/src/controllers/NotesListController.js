/**
 * @author Ruslan Yaniuk
 * @date June 2016
 */
(function (define) {
    "use strict";

    define(function () {

        var NotesListController = function ($scope, notes) {

            var onIsExpanded = function (note) {
                    if (note.expanded) {
                        return 'note-text-expanded';
                    }
                    return 'note-text-collapsed';
                },

                onGetExpandIcon = function (note) {
                    if (note.expanded == undefined || null || !note.expanded) {
                        return 'expand_more';
                    }
                    if (note.expanded) {
                        return 'expand_less';
                    }
                },

                onExpandCollapse = function (note) {
                    if (note.expanded == undefined || null) {
                        note.expanded = true;
                    } else {
                        note.expanded = !note.expanded;
                    }
                },

                onIsTextHolderOverloaded = function (note) {
                    var element = document.getElementById('text_' + note.id);

                    if (element.offsetHeight < element.scrollHeight ||
                        element.offsetWidth < element.scrollWidth) {
                        return true;
                    }
                    if (note.expanded) {
                        return true;
                    }
                    //element doesn't have overflow
                    return false;
                };

            $scope.isExpanded = onIsExpanded;
            $scope.getExpandIcon = onGetExpandIcon;
            $scope.isTextHolderOverloaded = onIsTextHolderOverloaded;
            $scope.expandCollapse = onExpandCollapse;

            $scope.$on('scrolledDown', notes.findNotesNextPage);
        };

        return ["$scope", "notes", NotesListController];
    });

})(define);