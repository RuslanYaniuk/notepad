/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var NoteController = function ($scope, $mdDialog, noteService, dispatcherService) {

            var onShowNewNoteDialog = function (ev) {
                    $scope.notesContainer.note.id = "";
                    $scope.notesContainer.note.subject = "";
                    $scope.notesContainer.note.text = "";

                    var newNoteDialog = function () {
                        $mdDialog.show({
                            controller: 'NoteController',
                            templateUrl: 'assets/views/app/note.create.dlg.html',
                            parent: angular.element(document.body),
                            targetEvent: ev
                        });
                    };

                    if (dispatcherService.isSearchPageCurrent()) {
                        var confirm = $mdDialog.confirm()
                            .title('You are in the search mode')
                            .content('To add a new note you need to quit the search mode')
                            .ok('Quit')
                            .cancel('Keep searching');

                        $mdDialog
                            .show(confirm)
                            .then(function onSelectYes() {
                                onExitSearchMode();
                                newNoteDialog();
                            });
                    } else {
                        newNoteDialog();
                    }
                },

                onShowEditDialog = function (ev, note) {
                    $scope.notesContainer.note.id = note.id;
                    $scope.notesContainer.note.subject = note.subject.valueOf();
                    $scope.notesContainer.note.text = note.text.valueOf();

                    $mdDialog.show({
                        controller: 'NoteController',
                        templateUrl: 'assets/views/app/note.edit.dlg.html',
                        parent: angular.element(document.body)
                    });
                },

                onCloseDialog = function () {
                    $mdDialog.hide();
                },

                onCreateNote = function () {
                    noteService.createNote();
                    $mdDialog.hide();
                },

                onUpdateNote = function () {
                    noteService.updateNote();
                    $mdDialog.hide()
                },

                onDeleteNote = function (note) {
                    var confirm = $mdDialog.confirm()
                        .title('Delete confirmation')
                        .content('Delete this note? All data will be lost.')
                        .ariaLabel('Delete note')
                        .ok('Delete')
                        .cancel('Cancel');
                    $mdDialog.show(confirm).then(function onSelectYes() {
                        noteService.deleteNote(note.id);
                    }, function onDecline() {
                    });

                },

                onGetFormattedText = function (note) {
                    return note.text;
                },

                onApplySearchOptions = function () {
                    if (!$scope.notesContainer.searchInText && !$scope.notesContainer.searchInSubject) {
                        $scope.notesContainer.searchInText = true;
                        $scope.notesContainer.searchInSubject = true;
                    }
                    onCloseDialog();
                },

                onIsExpanded = function (note) {
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
                },

                onSearchSubmit = function () {
                    noteService.clearNotesContainer();
                    noteService.search();
                    dispatcherService.goToSearchNotesPage();
                },

                onExitSearchMode = function () {
                    $scope.notesContainer.searchString = "";
                    dispatcherService.goToLatestNotesPage();
                },

                onGetLatest = function () {
                    noteService.getLatest();
                },

                onCompleteCurrentList = function () {
                    noteService.loadNextPageIfNeeded().then();
                };

            $scope.exitSearchMode = onExitSearchMode;

            $scope.showNewNoteDialog = onShowNewNoteDialog;
            $scope.showEditNoteDialog = onShowEditDialog;
            $scope.closeDialog = onCloseDialog;
            $scope.getLatest = onGetLatest;

            $scope.searchSubmit = onSearchSubmit;

            $scope.createNote = onCreateNote;
            $scope.updateNote = onUpdateNote;
            $scope.deleteNote = onDeleteNote;

            $scope.applySearchOptions = onApplySearchOptions;

            $scope.isExpanded = onIsExpanded;
            $scope.getExpandIcon = onGetExpandIcon;
            $scope.isTextHolderOverloaded = onIsTextHolderOverloaded;
            $scope.expandCollapse = onExpandCollapse;

            $scope.notesContainer = noteService.notesContainer;
            $scope.getFormattedText = onGetFormattedText;

            $scope.completeCurrentList = onCompleteCurrentList;
        };

        return [
            "$scope",
            "$mdDialog",
            "noteService",
            "dispatcherService", NoteController];
    });

})(define);