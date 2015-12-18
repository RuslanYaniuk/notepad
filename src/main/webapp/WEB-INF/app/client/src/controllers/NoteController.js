/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var NoteController = function ($scope, $mdDialog, $sce, noteService) {

            var onShowNewNoteDialog = function (ev) {
                    $scope.notesContainer.note.id = "";
                    $scope.notesContainer.note.subject = "";
                    $scope.notesContainer.note.text = "";

                    $mdDialog.show({
                        controller: NoteController,
                        templateUrl: 'assets/views/app/note.create.dlg.html',
                        parent: angular.element(document.body),
                        targetEvent: ev
                    });
                },

                onShowEditDialog = function (ev, note) {
                    $scope.notesContainer.note.id = note.id;
                    $scope.notesContainer.note.subject = note.subject.valueOf();
                    $scope.notesContainer.note.text = note.text.valueOf();

                    $mdDialog.show({
                        controller: NoteController,
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
                        .ok('Yes')
                        .cancel('Cancel');
                    $mdDialog.show(confirm).then(function onSelectYes() {
                        noteService.deleteNote(note.id);
                    }, function onDecline() {
                    });

                },

                onGetFormattedText = function (note) {
                    return note.text;
                },

                isValidNote = function (note) {
                    if (note.subject != undefined || "") {
                        return true;
                    }
                    if (note.text != undefined || "") {
                        return true;
                    }

                    return false;
                },

                init = function () {
                    noteService.getLatest();
                };

            $scope.showNewNoteDialog = onShowNewNoteDialog;
            $scope.showEditNoteDialog = onShowEditDialog;
            $scope.closeDialog = onCloseDialog;

            $scope.createNote = onCreateNote;
            $scope.updateNote = onUpdateNote;
            $scope.deleteNote = onDeleteNote;

            $scope.init = init;

            $scope.notesContainer = noteService.notesContainer;
            $scope.getFormattedText = onGetFormattedText;
        };

        return ["$scope", "$mdDialog", "$sce", "noteService", NoteController];
    });

})(define);