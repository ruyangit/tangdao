
const routes = [
  {
    path: '/user',
    component: () => import('layouts/UserLayout.vue'),
    children: [
      { path: '', redirect: { name: 'user-login' } },
      { path: 'login', name: 'user-login', component: () => import('pages/Login.vue') }
    ]
  },
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', redirect: { path: 'dashboard' } },
      { path: 'dashboard', meta: { auth: true }, component: () => import('pages/Index.vue') },
      {
        path: 'system',
        component: () => import('layouts/BlankLayout.vue'),
        children: [
          { path: '', redirect: { path: 'overview' } },
          { path: 'overview', meta: { sidebar: true, auth: true }, component: () => import('pages/Index.vue') },
          { path: 'role', meta: { sidebar: true, auth: true }, component: () => import('pages/system/Role.vue') },
          { path: 'role/form', meta: { sidebar: true, auth: true }, component: () => import('pages/system/RoleForm.vue') },
          { path: 'role/form/:id', meta: { sidebar: true, auth: true }, component: () => import('pages/system/RoleForm.vue') }

        ]
      }
    ]
  },

  // Always leave this as last one,
  // but you can also remove it
  {
    path: '*',
    component: () => import('pages/Error404.vue')
  }
]

export default routes
