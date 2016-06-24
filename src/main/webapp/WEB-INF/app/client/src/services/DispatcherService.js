/**
 * @author Ruslan Yaniuk
 * @date December 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var DispatcherService = function ($state) {

            var UI_STATE_NOTES_LATEST = "application.latestNotes",
                UI_STATE_SEARCH_NOTES = "application.searchNotes",
                UI_STATE_APPLICATION = "application",
                UI_STATE_INDEX = "index",
                UI_STATE_LOGIN = "login",
                UI_STATE_ACCESS_DENIED_ERROR = "access-denied";

            var onGoToLoginPage = function () {
                    $state.go(UI_STATE_LOGIN);
                },

                onGoToLatestNotesPage = function (option) {
                    $state.go(UI_STATE_NOTES_LATEST, null, option);
                },

                onGoToIndexPage = function () {
                    $state.go(UI_STATE_INDEX);
                },

                onGoToAccessDeniedErrorPage = function () {
                    $state.go(UI_STATE_ACCESS_DENIED_ERROR);
                },

                onGoToSearchNotesPage = function () {
                    $state.go(UI_STATE_SEARCH_NOTES);
                },

                onGoToApplicationPage = function (option) {
                    $state.go(UI_STATE_APPLICATION, null, option);
                },

                onIsSearchPageCurrent = function () {
                    return $state.current.name == UI_STATE_SEARCH_NOTES;
                },

                onGo = function (stateTo) {
                    $state.go(stateTo.name);
                };

            return {
                goToLoginPage: onGoToLoginPage,
                goToLatestNotesPage: onGoToLatestNotesPage,
                goToIndexPage: onGoToIndexPage,
                goToAccessDeniedErrorPage: onGoToAccessDeniedErrorPage,
                goToApplicationPage: onGoToApplicationPage,
                goToSearchNotesPage: onGoToSearchNotesPage,
                isSearchPageCurrent: onIsSearchPageCurrent,
                go: onGo
            }
        };

        return ["$state", DispatcherService];
    });

})(define);