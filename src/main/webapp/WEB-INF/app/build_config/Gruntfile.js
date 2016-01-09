module.exports = function(grunt) {

    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-requirejs');
    grunt.loadNpmTasks('grunt-contrib-less');

    /**
     * Load global variables
     */
    var userConfig = require('./build.config.js');

    var taskConfig = {

        pkg: grunt.file.readJSON("package.json"),

        clean: {
            src: [
                '<%= buildDir %>'
            ],
            hooks: [
            ],
            options: {
                force: true
            }
        },

        copy: {
            index: {
                files: [
                    {
                        src: '<%= devDir %>/index.html',
                        dest: '<%= buildDir %>/index.html'
                    }
                ]
            },

            assets: {
                files: [
                    {
                        src: [ '**', '!less/**', '!js/**' ],
                        cwd: '<%= devDir %>/assets',
                        dest: '<%= buildDir %>/assets/',
                        expand: true
                    }
                ]
            },

            production_boot: {
                files: [
                    {
                        src: '<%= devDir %>/assets/js/boot_prod.js',
                        dest: '<%= buildDir %>/assets/js/boot.js'
                    }
                ]
            },

            vendor_files: {
                files: [
                    {
                        src: [ '**/*' ],
                        cwd: '<%= devDir %>/vendor',
                        dest: '<%= buildDir %>/vendor/',
                        expand: true
                    }
                ]
            }
        },

        less: {
            compile_production : {
                files: {
                    "<%= buildDir %>/assets/css/main.css": "<%= devDir %>/assets/less/main.less"
                }
            }
        },

        /**
         * Minifies RJS files and makes it production ready
         * Build files are minified and encapsulated using RJS Optimizer plugin
         */
        requirejs: {
            compile: {
                options: {
                    baseUrl: "../client/src",
                    out: '<%= buildDir %>/src/notepad.min.js',
                    name: 'main'
                },
                preserveLicenseComments : false,
                optimize: "uglify"
            }
        }
    };

    grunt.initConfig(grunt.util._.extend(taskConfig, userConfig));

    grunt.registerTask( "prod", [
        'clean:src',
        'copy:assets',
        'less:compile_production',
        'copy:production_boot',
        'copy:vendor_files',
        'copy:index',
        "requirejs"
    ]);
};
