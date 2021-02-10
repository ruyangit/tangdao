import Vue from 'vue'

// https://webpack.js.org/guides/dependency-management/#require-context
const requireComponent = require.context('../components', true, /[a-z0-9]+\.(jsx?|vue)$/i)

const components = [
  './Status/Status.vue'
]
// For each matching file name...
components.forEach(fileName => {
  const componentConfig = requireComponent(fileName)
  const componentName = fileName
    .substr(fileName.lastIndexOf('/') + 1)
    // Remove the file extension from the end
    .replace(/\.\w+$/, '')
  // Globally register the component
  Vue.component('Q' + componentName, componentConfig.default || componentConfig)
})
