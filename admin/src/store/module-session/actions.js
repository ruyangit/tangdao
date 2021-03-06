import Vue from 'vue'
export function loginAction ({ commit }, data) {
  commit('resetMutation', { login: false })
  return Vue.prototype.$axios({ url: '/api/login', method: 'POST', type: 'FORM', data }).then(response => {
    const { code, data } = response.data
    if (code === '0' && data) {
      commit('loginMutation', data.access_token)
      return data
    }
    return Promise.reject(response.data)
  })
}

export function validateAction ({ commit, state }) {
  if (!state.access_token) return Promise.resolve(null)
  return Vue.prototype.$fetchData({ url: '/api/check_token', method: 'GET' }).then(response => {
    const { code, data } = response.data
    if (code === '0' && data) {
      commit('loginMutation', data.access_token)
      return data
    }
    return Promise.reject(response.data)
  })
}

export function menusAction () {
  //
}
