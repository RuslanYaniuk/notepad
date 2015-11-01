/**
 * @author Ruslan Yaniuk
 * @date November 2015
 */
(function (define) {
    "use strict";

    define(function () {

        var upperCase = new RegExp('[A-Z]'),
            lowerCase = new RegExp('[a-z]'),
            number = new RegExp('[0-9]'),
            specialCharacterAndSpace = new RegExp('[^a-zA-Z0-9 ]');

        var PasswordValidator = function () {
            return {
                restrict: "A",

                require: "ngModel",

                link: function (scope, element, attributes, ngModel) {
                    var checkPassword = function (modelValue) {
                        return upperCase.test(modelValue) &&
                            lowerCase.test(modelValue) &&
                            number.test(modelValue) &&
                            specialCharacterAndSpace.test(modelValue);
                    };

                    ngModel.$parsers.unshift(function (modelValue) {
                        var valid = checkPassword(modelValue);

                        ngModel.$setValidity('password', valid);

                        return valid ? modelValue : undefined;
                    });

                    ngModel.$formatters.unshift(function (modelValue) {
                        var valid = checkPassword(modelValue);

                        ngModel.$setValidity('password', valid);

                        return modelValue;
                    });
                }
            };
        };

        return PasswordValidator;
    });

})(define);