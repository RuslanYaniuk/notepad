/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
(function (define) {
    "use strict";

    var ROLE_USER = "ROLE_USER",
        ROLE_ADMIN = "ROLE_ADMIN",
        ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    define(function () {

        var UserRoleService = function () {

            var onContainsPermission = function (permissionSet, role) {
                    for (var i = 0; i < permissionSet.length; i++) {
                        if (permissionSet[i] == role) {
                            return true;
                        }
                    }
                    return false;
                },

                containsRole = function (rolesSet, role) {
                    for (var i = 0; i < rolesSet.length; i++) {
                        if (rolesSet[i].role == role) {
                            return true;
                        }
                    }
                    return false;
                },

                onContainsRoleAdmin = function (rolesSet) {
                    return containsRole(rolesSet, ROLE_ADMIN);
                },

                onContainsRoleUser = function (rolesSet) {
                    return containsRole(rolesSet, ROLE_USER);
                },

                onContainsRoleAnonymous = function (rolesSet) {
                    return containsRole(rolesSet, ROLE_ANONYMOUS);
                },

                onGetHighestRole = function (rolesSet) {
                    if (onContainsRoleAdmin(rolesSet)) {
                        return ROLE_ADMIN;
                    }

                    if (onContainsRoleUser(rolesSet)) {
                        return ROLE_USER;
                    }

                    if (onContainsRoleAnonymous(rolesSet)) {
                        return ROLE_ANONYMOUS;
                    }
                    throw new {message: "Could not parse roles"};
                },

                onIsAnonymous = function (role) {
                    return role == ROLE_ANONYMOUS;
                },

                onIsUser = function (role) {
                    return role == ROLE_USER;
                },

                onIsAdmin = function (role) {
                    return role == ROLE_ADMIN
                },

                onCompareUserRoles = function (userRolesA, userRolesB) {
                    var result = 0;

                    if (userRolesA.length != userRolesB.length) {
                        return false;
                    }

                    for (var i = 0; i < userRolesA.length; i++) {
                        for (var j = 0; j < userRolesA.length; j++) {
                            if (userRolesB[i].role == userRolesA[j].role) {
                                result++;
                            }
                        }
                    }
                    return result == userRolesA.length;
                };

            return {
                containsPermission: onContainsPermission,
                containsRoleAdmin: onContainsRoleAdmin,
                containsRoleUser: onContainsRoleUser,
                containsRoleAnonymous: onContainsRoleAnonymous,
                getHighestRole: onGetHighestRole,
                isAnonymous: onIsAnonymous,
                isUser: onIsUser,
                isAdmin: onIsAdmin,
                compareUserRoles: onCompareUserRoles
            }
        };

        return [UserRoleService];
    });

})(define);