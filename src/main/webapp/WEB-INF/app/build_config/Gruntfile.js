module.exports = function(grunt) {

    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-bower-task');

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
                        src: '<%= devDir %>/index.vm',
                        dest: '<%= buildDir %>/index.vm'
                    }
                ]
            },

            build_assets: {
                files: [
                    {
                        src: [ '**', '!less/!**' ],
                        cwd: '<%= devDir %>/assets',
                        dest: '<%= buildDir %>/assets/',
                        expand: true
                    }
                ]
            },

            sources: {
                files: [
                    {
                        src: [ '**/*.js' ],
                        cwd: '<%= devDir %>/src',
                        dest: '<%= buildDir %>/src/',
                        expand: true
                    }
                ]
            },

            build_vendorjs: {
                files: [
                    {
                        src: [ '**/*.js' ],
                        cwd: '<%= devDir %>/vendor',
                        dest: '<%= buildDir %>/assets/js/vendor/',
                        expand: true
                    }
                ]
            }
        },

        bower: {
            install: {
                options: {
                    verbose: true,
                    targetDir: '../client/assets/vendor',
                    cleanBowerDir: true,
                    layout: 'byType'
                }
            }
        }
    };

    grunt.initConfig(grunt.util._.extend(taskConfig, userConfig));

    /* Tasks */
    grunt.registerTask("prepare", [
        'clean:src',
        'bower:install'
    ]);

    grunt.registerTask("dev", [
        'clean:src',
        'copy:build_assets',
        'copy:sources',
        'copy:build_vendorjs',
        'copy:index'
    ]);

    grunt.registerTask( "prod", [
        'clean:src',
        'copy:build_assets',
        'copy:build_vendorjs',
        'copy:prod_boot',
        'copy:index',
        "requirejs",
        "concat:source"
    ]);

    function stripBanner( src ) {
        var m = [
                '(?:.*\\/\\/.*\\r?\\n)*\\s*',   // Strip // ... leading banners.
                '\\/\\*[\\s\\S]*?\\*\\/'        // Strips all /* ... */ block comment banners.
            ],
            re = new RegExp('^\\s*(?:' + m.join('|') + ')\\s*', '');

        return src.replace(re, '', "gm");
    };
};
