'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',

  VUE_HY_WEB: '"http://124.70.200.174:9527"',
  PICTURE_API: '"http://124.70.200.174:8602"',
  WEB_API: '"http://124.70.200.174:8603"',
  Search_API: '"http://124.70.200.174:8605"',
	ELASTICSEARCH: '"http://124.70.200.174:8605"',

})
