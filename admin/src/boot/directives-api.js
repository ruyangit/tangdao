/**
 * We register all the Directives so future cli-ui plugins
 * could use them directly
 */

import Vue from 'vue'

const requireDirective = require.context('../directives', true, /[a-z0-9]+\.(js)$/i)
requireDirective.keys().forEach(fileName => {
  const directiveConfig = requireDirective(fileName)
  const d = directiveConfig.default || directiveConfig
  if (d.name !== undefined && d.unbind !== undefined) {
    Vue.directive(d.name, d)
  }
})
