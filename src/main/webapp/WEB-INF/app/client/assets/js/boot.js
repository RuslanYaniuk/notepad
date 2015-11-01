(function (head) {
    "use strict";

    head.js(

        { require       : "assets/vendor/requirejs/require.js" },
        { underscore    : "assets/vendor/underscore/underscore.js" },

        { angular       : "assets/vendor/angular/angular.js" },
        { ng_route      : "assets/vendor/angular-ui-router/angular-ui-router.js" },
        { ng_aria       : "assets/vendor/angular-aria/angular-aria.js" },
        { ng_material   : "assets/vendor/angular-material/angular-material.js" },
        { ng_animate    : "assets/vendor/angular-animate/angular-animate.js" },
        { ng_table      : "assets/vendor/angular-material-data-table/md-data-table.min.js" },
        { ng_messages   : "assets/vendor/angular-messages/angular-messages.js" },
        { ng_loading_bar: "assets/vendor/angular-loading-bar/loading-bar.js"}


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