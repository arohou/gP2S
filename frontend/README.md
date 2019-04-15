# gP2S UI
Essentials information and links to install and run the front-end part of the application.

## Getting started
* [Requirements](#requirements)
* [Installing and running](#installing-and-running)
* [Browser compatibility](#browser-compatibility)

### Requirements
What you need to run the application:
- [nodejs 8.x](https://nodejs.org)
- [git 2.x](https://git-scm.com/downloads)
- [Python 2.6+](https://www.python.org)

# Technology Stack
Application uses:
- [Vue.js](https://vuejs.org) - JavaScript framework
- [vue-cli](https://github.com/vuejs/vue-cli) - simple CLI for scaffolding Vue.js projects based on [webpack](https://github.com/vuejs-templates/webpack) template
- [Element](element.eleme.io) - UI framework for Vue.js
- [SASS](http://sass-lang.com/) - CSS pre-processor
- [Karma](http://karma-runner.github.io) - unit tests runner
- [Istanbul](https://github.com/gotwarlost/istanbul) - unit tests coverage reporting
- [Node.js](https://nodejs.org) - asynchronous event driven JavaScript runtime

# Architecture
Front-end architecture is based on [vue-cli](https://github.com/vuejs/vue-cli) tool which is an official command line interface for Vue.js. Common questions specific to this architecture are answered and each part is described in greater detail [here](http://vuejs-templates.github.io/webpack/).

### Running for development
Run the following commands in your console to install and start the application:

```bash
# install all required dependencies
$ npm install

# start the application in development mode
$ npm run start

# run unit tests
$ npm run test:unit
```

## Building project for deployment
To build project use below command:
```bash
# build for prod
npm run build:prod
```

>It will start the application (`http://localhost:8080`).

>**WINDOWS USERS**: Node 6.x+ is required to run the application properly. Be sure to download the latest 6.x version from http://nodejs.org. Moreover, don't use power shell or any other client tools to avoid any unnecessary issues.
 
## Configuration

- **/config** - contains all related webpack's config files for different environments like dev.env (for development), test.env (for testing) and prod.env (for production)
- **/src/config** - contains all related applications' config files like messages etc.
> **Please note**: all specific variables related to the environment (e.g. endpoints) are kept in /config files because of webpack define plugin functionality. Any other configuration (like default pagination etc.) should be kept in /src/config folder.

# Browser compatibility
The application should be compatible with the following browsers:
- IE11+
- Chrome 35+
- Safari 10+
- iOS Safari 10.2+
