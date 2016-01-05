/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var NoteService = function ($http, $q) {

            var ITEMS_PER_PAGE = 10;

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

                onGetLatest = function () {
                    currentLoader = onGetLatest;

                    return $http({
                        url: "/api/note/get-latest",
                        method: "GET",
                        params: {
                            pageNumber: _notesContainer.page.number,
                            pageSize: ITEMS_PER_PAGE
                        }
                    }).then(function onGetLatest_Success(response) {
                        _notesContainer.notes = _notesContainer.notes.concat(response.data.content);
                        _notesContainer.page = response.data;
                    });
                },

                onSearch = function () {
                    currentLoader = onSearch;

                    var requestParams = {
                        'pageNumber': _notesContainer.page.number,
                        'pageSize': ITEMS_PER_PAGE
                    };

                    if (_notesContainer.searchInSubject) {
                        requestParams.subject = _notesContainer.searchString;
                    }
                    if (_notesContainer.searchInText) {
                        requestParams.text = _notesContainer.searchString;
                    }

                    return $http({
                        url: "/api/note/find",
                        method: "GET",
                        params: requestParams
                    }).then(function onGetLatest_Success(response) {
                        _notesContainer.notes = _notesContainer.notes.concat(response.data.content);
                        _notesContainer.page = response.data;
                        _notesContainer.page.requestParams = requestParams;
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

                onLoadNextPageIfNeeded = function () {
                    if (_notesContainer.page.last) {
                        return $q.when({});
                    }
                    _notesContainer.page.number++;
                    return currentLoader();
                },

                onClearNotesContainer = function () {
                    _notesContainer.notes = [];
                    _notesContainer.page = {
                        first: true,
                        last: false,
                        number: 0,
                        numberOfElements: 0,
                        size: 0,
                        totalPages: 0
                    };
                },

                currentLoader = $q.when({}),

                _notesContainer = {
                    notes: [],

                    page: {
                        first: true,
                        last: false,
                        number: 0,
                        numberOfElements: 0,
                        size: 0,
                        totalElements: 0,
                        totalPages: 0
                    },

                    note: {
                        id: "",
                        subject: "",
                        text: "",
                        creationDate: ""
                    },

                    searchString: "",
                    searchInText: true,
                    searchInSubject: true
                };

            return {
                createNote: onCreateNote,
                search: onSearch,
                getLatest: onGetLatest,
                updateNote: onUpdateNote,
                deleteNote: onDeleteNote,
                clearNotesContainer: onClearNotesContainer,
                loadNextPageIfNeeded: onLoadNextPageIfNeeded,
                notesContainer: _notesContainer
            }
        };

        return ["$http", "$q", NoteService];
    });

})(define);