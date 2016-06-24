/**
 * @author Ruslan Yaniuk
 * @date June 2016
 */
(function (define) {
    "use strict";

    define(function () {

        var MainToolBarService = function () {
            var _state = {
                searchMode: false
            };

            return {
                state: _state
            }
        };

        return [MainToolBarService];
    });

})(define);