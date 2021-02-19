
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
      { path: 'dashboard', meta: { sidebar: false, title: '控制台' }, component: () => import('pages/Index.vue') },
      {
        path: 'system',
        component: () => import('layouts/BlankLayout.vue'),
        children: [
          { path: '', redirect: { path: 'overview' } },
          { path: 'overview', meta: {}, component: () => import('pages/Index.vue') },
          { path: 'role', meta: { title: '角色' }, component: () => import('pages/system/RoleList.vue') },
          { path: 'role/form', meta: {}, component: () => import('pages/system/RoleForm.vue') },
          { path: 'role/form/:id', meta: {}, component: () => import('pages/system/RoleForm.vue') },
          { path: 'menu', meta: {}, component: () => import('pages/system/MenuIndex.vue') }

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
