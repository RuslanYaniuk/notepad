/**
 *  Run 'webdriver-manager start' before running protractor
 *
 * @author Ruslan Yaniuk
 * @date July 2015
 */
exports.config = {
    seleniumAddress: 'http://localhost:4444/wd/hub',
    specs: [
        '../spec/e2e/LoginSpec.js',
        '../spec/e2e/AppSpec.js'
    ],

    capabilities: {
        'browserName': 'chrome'
    },

    baseUrl: 'http://localhost:8080/'
};