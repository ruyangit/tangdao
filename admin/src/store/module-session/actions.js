import Vue from 'vue'
export function loginAction ({ commit, dispatch, getters }, data) {
  return Vue.prototype.$fetchData({ url: '/api/user/login' })
}

export function menusAction ({ commit, state }) {
  // const menusOld = state.menus
  // commit('menusMutation', [])

  // // commit('basic/globalLoadingMutAction', true)
  // Vue.prototype.$fetchData({ url: '/login' }).then(e => {
  //   console.log(e)
  // }).finally(() => {
  //   commit('menusMutation', menusOld)
  //   // commit('basic/globalLoadingMutAction', false)
  // })
}
