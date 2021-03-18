
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
      { path: '', redirect: { path: 'sms/overview' } },
      {
        path: 'sms',
        component: () => import('layouts/BlankLayout.vue'),
        children: [
          { path: '', redirect: { path: 'overview' } },
          { path: 'overview', meta: {}, component: () => import('pages/Index.vue') },
          { path: 'record', meta: {}, component: () => import('pages/sms/MtMessageSubmitList.vue') },
          { path: 'fail/record', meta: {}, component: () => import('pages/system/RoleList.vue') },
          { path: 'rev/record', meta: {}, component: () => import('pages/system/RoleForm.vue') },
          { path: 'receipt/analysis', meta: {}, component: () => import('pages/system/RoleForm.vue') }

        ]
      },
      {
        path: 'user',
        component: () => import('layouts/BlankLayout.vue'),
        children: [
          { path: '', redirect: { path: 'record' } },
          { path: 'record', meta: {}, component: () => import('pages/sms/MtMessageSubmitList.vue') }
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
