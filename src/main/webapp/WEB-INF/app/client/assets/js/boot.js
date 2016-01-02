/**
 * Development version of boot file
 */
(function (head) {
    "use strict";

    head.js(

        { require       : "vendor/requirejs/require.js" },
        { underscore    : "vendor/underscore/underscore.js" },

        { angular       : "vendor/angular/angular.js" },
        { ng_ui_router  : "vendor/angular-ui-router/angular-ui-router.js" },
        { ng_sanitize   : "vendor/angular-sanitize/angular-sanitize.js" },
        { ng_aria       : "vendor/angular-aria/angular-aria.js" },
        { ng_material   : "vendor/angular-material/angular-material.js" },
        { ng_animate    : "vendor/angular-animate/angular-animate.js" },
        { ng_table      : "vendor/angular-material-data-table/md-data-table.js" },
        { ng_messages   : "vendor/angular-messages/angular-messages.js" },
        { ng_loading_bar: "vendor/angular-loading-bar/loading-bar.js"}


    ).ready("ALL", function() {

            require.config (
                {
                    appDir  : '',
                    baseUrl : './src'
                });

            require( [ "main" ], function( app )
            {
                // Application's entry point
            });
        });

})(window.head);