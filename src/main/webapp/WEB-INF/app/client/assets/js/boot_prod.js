/**
 * This file will be renamed to boot.js
 * for production version
 */
(function (head) {
    "use strict";

    head.js(

        { require       : "vendor/requirejs/require.js" },
        { underscore    : "vendor/underscore/underscore-min.js" },

        { angular       : "vendor/angular/angular.min.js" },
        { ng_ui_router  : "vendor/angular-ui-router/angular-ui-router.min.js" },
        { ng_sanitize   : "vendor/angular-sanitize/angular-sanitize.min.js" },
        { ng_aria       : "vendor/angular-aria/angular-aria.min.js" },
        { ng_material   : "vendor/angular-material/angular-material.min.js" },
        { ng_animate    : "vendor/angular-animate/angular-animate.min.js" },
        { ng_table      : "vendor/angular-material-data-table/md-data-table.min.js" },
        { ng_messages   : "vendor/angular-messages/angular-messages.min.js" },
        { ng_loading_bar: "vendor/angular-loading-bar/loading-bar.min.js"},

        { notepad       : "src/notepad.min.js"}


    ).ready("ALL", function() {

            require( [ "main" ], function( app )
            {
                // Application's entry point
            });
        });

})(window.head);