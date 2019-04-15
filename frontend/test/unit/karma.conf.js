// This is a karma config file. For more details see
//   http://karma-runner.github.io/0.13/config/configuration-file.html
// we are also using it with karma-webpack
//   https://github.com/webpack/karma-webpack

var grep = require('karma-webpack-grep')
var webpackConfig = require('../../build/webpack.test.conf')

process.env.CHROME_BIN = require('puppeteer').executablePath()

module.exports = config => {
  webpackConfig.plugins = (webpackConfig.plugins || []).concat(grep({
    grep: config.grep,
    basePath: '.',
    testContext: './specs'
  }))

  config.set({
    // to run in additional browsers:
    // 1. install corresponding karma launcher
    //    http://karma-runner.github.io/0.13/config/browsers.html
    // 2. add it to the `browsers` array below.
    browsers: ['HeadlessChrome', 'PhantomJS'],
    frameworks: ['mocha', 'phantomjs-shim',
      'sinon-chai', 'chai-string', 'chai-as-promised', 'chai-dom', 'chai'
    ],
    reporters: ['mocha', 'coverage', 'junit'],
    files: ['./index.js'],
    preprocessors: {
      // Removing sourcemap to make test runs much faster. If nobody cries,
      // I'll remove it completely.
      './index.js': ['webpack'/*, 'sourcemap'*/],
    },
    plugins: [
      // Utils
      'karma-phantomjs-shim',

      // Launchers
      'karma-chrome-launcher',
      'karma-phantomjs-launcher',

      // Test Libraries
      'karma-mocha',
      'karma-sinon-chai',
      'karma-chai-as-promised',
      'karma-chai-string',
      'karma-chai-dom',
      'karma-chai',

      // Preprocessors
      'karma-webpack',
      'karma-sourcemap-loader',

      // Reporters
      'karma-mocha-reporter',
      'karma-coverage',
      'karma-junit-reporter'
    ],
    client: {
      mocha: {
        timeout: 10000
      }
    },
    webpack: webpackConfig,
    webpackMiddleware: {
      noInfo: true
    },
    customLaunchers: {
      HeadlessChrome: {
        base: 'ChromeHeadless',
        flags: [
          '--no-sandbox',
          '--headless',
          '--enable-gpu',
          '--disable-translate',
          '--disable-extensions',
          '--disable-web-security'
        ]
      }
    },
    browserNoActivityTimeout: 120000,
    coverageReporter: {
      dir: './coverage',
      reporters: [
        {type: 'lcov', subdir: '.'},
        {type: 'text-summary'}
      ]
    },
    mochaReporter: {
      showDiff: true
    },
    junitReporter: {
      outputDir: 'junit',
      outputFile: 'junit.xml',
      suite: 'gp2s-frontend'
    }
  })
}
