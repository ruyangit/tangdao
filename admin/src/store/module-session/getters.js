export const isAuthenticated = (state) => {
  return !!state.access_token
}

export const sidebarMenus = (state) => {
  return state.menus
}
