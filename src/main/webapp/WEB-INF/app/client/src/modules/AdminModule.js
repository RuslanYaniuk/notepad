/**
 * @author Ruslan Yaniuk
 * @date October 2015
 */
(function (define) {
    "use strict";

    var dependencies = [
        "controllers/AdminController",
        "services/AdminService"
    ];

    define(dependencies, function (AdminController, AdminService) {
        var moduleName = "mynote.AdminModule",
            modules = [
                'md.data.table'
            ];

        angular.module(moduleName, modules)
            .controller('AdminController', AdminController)
            .service('adminService', AdminService);

        return moduleName;
    });

})(define);