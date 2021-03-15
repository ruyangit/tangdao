export function loginMutation (state, token) {
  state.access_token = token
}

export function resetMutation (state, { code, message, login }) {
  state.access_token = null
  state.reset.login = login
  state.reset.code = code
  state.reset.message = message
}
export function menusMutation (state, menus) {
  state.menus = menus
}
