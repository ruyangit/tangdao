export const isAuthenticated = (state) => {
  return !!state.access_token
}

export const menus = (state) => {
  return state.menus
}

export const reset = (state) => {
  return state.reset
}
