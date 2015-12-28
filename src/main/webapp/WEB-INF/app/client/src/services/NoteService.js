/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var NoteService = function ($http) {

            var onCreateNote = function () {
                    var noteDTO = {
                        subject: _notesContainer.note.subject,
                        text: _notesContainer.note.text
                    };

                    $http.put("/api/note/create", noteDTO)
                        .then(function onCreateNote_Success(response) {
                            _notesContainer.notes.unshift(response.data);
                        });
                },

                onUpdateNote = function () {
                    var noteUpdateDTO = {
                        id: _notesContainer.note.id,
                        subject: _notesContainer.note.subject,
                        text: _notesContainer.note.text
                    };

                    return $http.post("/api/note/update", noteUpdateDTO)
                        .then(function onUpdateNote_Success() {
                            var noteIndex = getNoteIndex(_notesContainer.note.id);

                            _notesContainer.notes[noteIndex].text = _notesContainer.note.text;
                            _notesContainer.notes[noteIndex].subject = _notesContainer.note.subject;
                        });
                },

                onGetLatest = function () {
                    return $http({
                        url: "/api/note/get-latest",
                        method: "GET",
                        params: {pageNumber: 0, pageSize: 20}
                    }).then(function onGetLatest_Success(response) {
                        _notesContainer.notes = response.data.content;
                    });
                },

                onDeleteNote = function (noteId) {
                    return $http({
                        method: "DELETE",
                        url: "/api/note/delete",
                        data: {id: noteId},
                        headers: {'Content-Type': 'application/json'}
                    }).then(function onDeleteNote_Success() {
                        var noteIndex = getNoteIndex(noteId);

                        _notesContainer.notes.splice(noteIndex, 1);
                    });
                },

                onSearch = function (searchString) {
                    return $http({
                        url: "/api/note/find",
                        method: "GET",
                        params: {subject: searchString, text: searchString}
                    }).then(function onGetLatest_Success(response) {
                        _notesContainer.notes = response.data.content;
                        _notesContainer.searchMode = true;
                    });
                },

                getNoteIndex = function (noteId) {
                    var length = _notesContainer.notes.length;

                    for (var i = 0; i < length; i++) {
                        if (_notesContainer.notes[i].id == noteId) {
                            return i;
                        }
                    }
                    return -1;
                },

                _notesContainer = {
                    notes: [],
                    note: {
                        id: "",
                        subject: "",
                        text: "",
                        creationDate: ""
                    },
                    searchMode: false,
                    searchString: ""
                };

            return {
                search: onSearch,
                createNote: onCreateNote,
                updateNote: onUpdateNote,
                getLatest: onGetLatest,
                deleteNote: onDeleteNote,
                notesContainer: _notesContainer
            }
        };

        return ["$http", NoteService];
    });

})(define);