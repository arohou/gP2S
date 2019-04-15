var merge = require('webpack-merge')
var prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  API_URL: '"/gp2s/api/v1/"',
  PEOPLE_DATA_SYSTEM_EXISTS: false,
  SECURITY_ENABLED: false
})
