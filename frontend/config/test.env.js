var merge = require('webpack-merge')
var devEnv = require('./dev.env')

module.exports = merge(devEnv, {
  NODE_ENV: '"testing"',
  API_URL: '"/gp2s/api/v1/"',
  PEOPLE_DATA_SYSTEM_EXISTS: false,
  SECURITY_ENABLED: true
})
