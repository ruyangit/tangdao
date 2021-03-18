
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
      { path: '', redirect: { name: 'overview' } },
      { path: 'overview', name: 'overview', meta: {}, component: () => import('pages/Index.vue') },
      { path: 'sms/record', name: 'sms-record', meta: {}, component: () => import('pages/sms/MtMessageSubmitList.vue') }
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
