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
                    if ((_notesContainer.lastPage.number != 0) && _notesContainer.searchMode) {
                        clearNotesContainer();
                    }

                    if (_notesContainer.lastPage.last) {
                        return $q.when({});
                    }

                    return $http({
                        url: "/api/note/get-latest",
                        method: "GET",
                        params: {pageNumber: _notesContainer.lastPage.number, pageSize: ITEMS_PER_PAGE}
                    }).then(function onGetLatest_Success(response) {
                        _notesContainer.notes = _notesContainer.notes.concat(response.data.content);
                        _notesContainer.lastPage = response.data;
                        _notesContainer.lastPage.number += 1;
                    });
                },

                onSearch = function () {
                    var searchString = _notesContainer.searchString;
                    var requestParams;

                    clearNotesContainer();

                    requestParams = {
                        'pageNumber': _notesContainer.lastPage.number,
                        'pageSize': ITEMS_PER_PAGE
                    };

                    if (_notesContainer.searchInSubject) {
                        requestParams.subject = searchString;
                    }
                    if (_notesContainer.searchInText) {
                        requestParams.text = searchString;
                    }

                    return $http({
                        url: "/api/note/find",
                        method: "GET",
                        params: requestParams
                    }).then(function onGetLatest_Success(response) {
                        _notesContainer.notes = response.data.content;
                        _notesContainer.lastPage = response.data;
                        _notesContainer.searchMode = true;
                        _notesContainer.lastPage.requestParams = requestParams;
                    });
                },

                onSearchScroll = function () {
                    var requestParams = _notesContainer.lastPage.requestParams;

                    if (_notesContainer.lastPage.last) {
                        return $q.when({});
                    }

                    return $http({
                        url: "/api/note/find",
                        method: "GET",
                        params: requestParams
                    }).then(function onGetLatest_Success(response) {
                        _notesContainer.notes = _notesContainer.notes.concat(response.data.content);
                        _notesContainer.lastPage = response.data;
                        _notesContainer.lastPage.number += 1;
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

                clearNotesContainer = function () {
                    _notesContainer.notes = [];
                    _notesContainer.lastPage = {
                        first: true,
                        last: false,
                        number: 0,
                        numberOfElements: 0,
                        size: 0,
                        totalPages: 0
                    };
                },

                _notesContainer = {
                    notes: [],
                    lastPage: {
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
                    searchMode: false,
                    searchString: "",
                    searchInText: true,
                    searchInSubject: true
                };

            return {
                search: onSearch,
                createNote: onCreateNote,
                updateNote: onUpdateNote,
                getLatest: onGetLatest,
                deleteNote: onDeleteNote,
                searchScroll: onSearchScroll,
                notesContainer: _notesContainer
            }
        };

        return ["$http", "$q", NoteService];
    });

})(define);