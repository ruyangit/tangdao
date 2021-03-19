import { SessionStorage } from 'quasar'

export function loginMutation (state, token) {
  state.access_token = token
  SessionStorage.set('access_token', token)
}

export function resetMutation (state, { code, message, login }) {
  state.reset.login = login
  state.reset.code = code
  state.reset.message = message

  state.access_token = null
  SessionStorage.remove('access_token')
}

export function menusMutation (state, menus) {
  state.menus = menus
}
