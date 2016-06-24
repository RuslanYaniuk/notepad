/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var NoteController = function ($scope, $mdDialog, notes, mainToolBar) {

            var onShowNewNoteDialog = function (ev) {
                    $scope.notes.note.id = "";
                    $scope.notes.note.subject = "";
                    $scope.notes.note.text = "";

                    $mdDialog.show({
                        controller: 'NoteController',
                        templateUrl: 'assets/views/app/note.create.dlg.html',
                        parent: angular.element(document.body),
                        targetEvent: ev
                    });
                },

                onShowUpdateDialog = function (ev, note) {
                    $scope.notes.note.id = note.id;
                    $scope.notes.note.subject = note.subject.valueOf();
                    $scope.notes.note.text = note.text.valueOf();

                    $mdDialog.show({
                        controller: 'NoteController',
                        templateUrl: 'assets/views/app/note.edit.dlg.html',
                        parent: angular.element(document.body)
                    });
                },

                onCreateNote = function () {
                    notes.createNote().then(function onSuccess() {
                        $mdDialog.hide();
                    });
                },

                onUpdateNote = function () {
                    notes.updateNote().then(function onSuccess() {
                        $mdDialog.hide()
                    });
                },

                onDeleteNote = function (note) {
                    var confirm = $mdDialog.confirm()
                        .title('Delete confirmation')
                        .content('Delete this note? All data will be lost.')
                        .ariaLabel('Delete note')
                        .ok('Delete')
                        .cancel('Cancel');
                    $mdDialog.show(confirm).then(function onSelectYes() {
                        notes.deleteNote(note.id);
                    }, function onDecline() {
                    });
                };

            $scope.showNewNoteDialog = onShowNewNoteDialog;
            $scope.showUpdateDialog = onShowUpdateDialog;

            $scope.createNote = onCreateNote;
            $scope.updateNote = onUpdateNote;
            $scope.deleteNote = onDeleteNote;

            $scope.notes = notes.data;

            $scope.mainToolBar = mainToolBar;
            $scope.dialog = $mdDialog;
        };

        return [
            "$scope",
            "$mdDialog",
            "notes",
            "mainToolBar", NoteController];
    });

})(define);