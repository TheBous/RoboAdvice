// Karma configuration
// Generated on Thu Mar 16 2017 09:55:20 GMT+0100 (CET)

module.exports = function(config) {
    config.set({

        // base path that will be used to resolve all patterns (eg. files, exclude)
        basePath: '',


        // frameworks to use
        // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
        frameworks: ['jasmine'],
        webpack: {
            module: {
                loaders: [
                    { test: /\.js/, loader: 'baber-loader' }
                ]
            },
            watch: true
        },
        // list of files / patterns to load in the browser
        files: [
            './bower_components/angular/angular.js',
            './bower_components/angular-mocks/angular-mocks.js',
            './bower_components/angular-animate/angular-animate.js',
            './bower_components/angular-aria/angular-aria.js',
            './bower_components/chart.js/dist/Chart.js',
            './bower_components/angular-chart.js/dist/angular-chart.min.js',
            './bower_components/angular-cookies/angular-cookies.js',
            './bower_components/angular-material/angular-material.js',
            './bower_components/angular-messages/angular-messages.js',
            './bower_components/angular-resource/angular-resource.js',
            './bower_components/angular-route/angular-route.js',
            './bower_components/angular-wizard/dist/angular-wizard.js',
            './bower_components/angular-loading-bar/build/loading-bar.js',
            '.bower_components/sweetalert/dist/sweetalert.min.js',
            './js/app.js',
            './js/CONFIG.js',
            './js/STATUS_CODES.js',
            './js/routes/main_routes.js',
            './js/utils.js',
            './js/services/**/*.js',
            './js/class/strategy.js',
            './js/class/portfolio.js',
            './units/**/*.js',

        ],
        // list of files to exclude
        exclude: [
        ],


        // preprocess matching files before serving them to the browser
        // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
        preprocessors: {
			'./js/**/*.js' : 'coverage'
		  },


        // test results reporter to use
        // possible values: 'dots', 'progress'
        // available reporters: https://npmjs.org/browse/keyword/karma-reporter
        reporters: ['progress','coverage'],


        // web server port
        port: 9876,


        // enable / disable colors in the output (reporters and logs)
        colors: true,


        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,


        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,


        // start these browsers
        // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
        browsers: ['Chrome'],


        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: false,

		  coverageReporter: {
	      type : 'html',
   	   dir : './coverage/'
	    },

        // Concurrency level
        // how many browser should be started simultaneous
        concurrency: Infinity
    })
}
