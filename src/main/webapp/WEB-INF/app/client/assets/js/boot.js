(function (head) {
    "use strict";

    head.js(

        { require    : "assets/vendor/requirejs/require.js" },
        { underscore : "assets/vendor/underscore/underscore.js" },

        { angular    : "assets/vendor/angular/angular.js" },
        { ngRoute    : "assets/vendor/angular-ui-router/angular-ui-router.js" },
        { jquery     : "assets/vendor/jquery/jquery.js" },
        { ng_aria    : "assets/vendor/angular-aria/angular-aria.js" },
        { ng_animate : "assets/vendor/angular-animate/angular-animate.js" },
        { ng_material: "assets/vendor/angular-material/angular-material.js" },
        { ng_table   : "assets/vendor/angular-material-data-table/md-data-table.min.js" }


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