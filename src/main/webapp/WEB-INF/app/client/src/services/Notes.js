/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var NoteService = function ($http) {
            var ITEMS_PER_PAGE = 10;

            var onCreateNote = function () {
                    return $http.put("/api/note/create",
                        {
                            subject: _data.note.subject,
                            text: _data.note.text
                        })
                        .then(function onSuccess(response) {
                            _data.list.unshift(response.data);
                        });
                },

                onUpdateNote = function () {
                    return $http.post("/api/note/update",
                        {
                            id: _data.note.id,
                            subject: _data.note.subject,
                            text: _data.note.text
                        })
                        .then(function onUpdateNote_Success() {
                            var noteIndex = indexOf(_data.note.id);

                            _data.list[noteIndex].text = _data.note.text;
                            _data.list[noteIndex].subject = _data.note.subject;
                        });
                },

                onDeleteNote = function (noteId) {
                    return $http({
                        method: "DELETE",
                        url: "/api/note/delete",
                        data: {
                            id: noteId
                        },
                        headers: {'Content-Type': 'application/json'}
                    }).then(function onDeleteNote_Success() {
                        _data.list.splice(indexOf(noteId), 1);
                    });
                },

                indexOf = function (noteId) {
                    for (var i = 0; i < _data.list.length; i++) {
                        if (_data.list[i].id == noteId) {
                            return i;
                        }
                    }
                    return -1;
                },

                onFindNotes = function () {
                    _page.first = true;
                    _page.last = false;
                    _page.number = 0;
                    _page.numberOfElements = 0;
                    _page.size = ITEMS_PER_PAGE;
                    _page.totalPages = 0;
                    return findNotes().then(function onSuccess(response) {
                        _data.list = response.data.content;
                    });
                },

                onFindNotesNextPage = function () {
                    if (!_page.last) {
                        _page.number++;
                        return findNotes().then(function onSuccess(response) {
                            _data.list = _data.list.concat(response.data.content)
                        });
                    }
                },

                findNotes = function () {
                    return $http({
                        url: "/api/note/find",
                        method: "GET",
                        params: new query()
                    }).then(function onSuccess(response) {
                        var data = response.data;

                        _page.first = data.first;
                        _page.last = data.last;
                        _page.number = data.number;
                        _page.numberOfElements = data.numberOfElements;
                        _page.size = data.size;
                        _page.totalPages = data.totalPages;
                        return response;
                    });
                },

                query = function () {
                    var text = _query.text == "" ? null : _query.text;

                    this.pageNumber = _page.number;
                    this.pageSize = _page.size;
                    this.text = _query.searchInText ? text : null;
                    this.subject = _query.searchInSubject ? text : null;
                },

                _query = {
                    text: null,
                    searchInText: true,
                    searchInSubject: true
                },

                _page = {
                    first: true,
                    last: false,
                    number: 0,
                    numberOfElements: 0,
                    size: ITEMS_PER_PAGE,
                    totalElements: 0,
                    totalPages: 0
                },

                _data = {
                    list: [],
                    note: {
                        id: "",
                        subject: "",
                        text: "",
                        creationDate: ""
                    }
                };

            return {
                createNote: onCreateNote,
                updateNote: onUpdateNote,
                deleteNote: onDeleteNote,
                findNotes: onFindNotes,
                findNotesNextPage: onFindNotesNextPage,

                data: _data,
                query: _query
            }
        };

        return ["$http", NoteService];
    });

})(define);