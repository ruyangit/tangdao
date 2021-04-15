export default ({ app, router, Vue }) => {
  // Check for protected and guest routes and perform checks
  router.beforeEach((to, from, next) => {
    // Vue.prototype.$q.loadingBar.start()
    return next()
    // const protectedRoute = to.matched.some(route => route.meta.auth)
    // // Allow guest routes
    // if (!protectedRoute) return next()

    // // If auth is required and the user is logged in, verify the token...
    // if (app.store.getters['session/isAuthenticated']) {
    //   return app.store.dispatch('session/validate').then(user => {
    //     user ? next() : next({ name: 'user-login', query: { 'redirect': to.fullPath } })
    //   })
    // }
    // next({ name: 'user-login', query: { 'redirect': to.fullPath } })
  })

  router.afterEach((to, from, next) => {
    // setTimeout(() => {
    //   Vue.prototype.$q.loadingBar.stop()
    // }, 1500)
  })
}
