
const routes = [
  {
    path: '/403',
    component: () => import('pages/Error403.vue')
  },
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
      { path: 'single', name: 'single-page', meta: { sidebar: true }, component: () => import('pages/SinglePage.vue') },
      { path: 'overview', name: 'overview', component: () => import('pages/Index.vue') },
      {
        path: 'sys',
        component: () => import('layouts/BlankLayout.vue'),
        children: [
          { path: '', redirect: { name: 'overview' } },
          { path: 'user', name: 'user', component: () => import('pages/system/UserList.vue') },
          { path: 'user/form', name: 'userForm', component: () => import('pages/system/UserForm.vue') },
          { path: 'role', name: 'role', component: () => import('pages/system/RoleList.vue') },
          { path: 'config', name: 'config', component: () => import('pages/system/ConfigList.vue') },
          { path: 'dictType', name: 'dictType', component: () => import('src/pages/system/DictTypeList.vue') },
          { path: 'dictType/dictData/:dictType', name: 'dictTypeData', component: () => import('src/pages/system/DictDataList.vue') },
          { path: 'log', name: 'log', component: () => import('pages/system/LogList.vue') },
          { path: 'job', name: 'job', component: () => import('pages/form/AdvancedForm.vue') }
        ]
      },
      { path: 'icons', name: 'icons', component: () => import('src/pages/form/Icons.vue') },
      { path: 'server', name: 'server', component: () => import('src/pages/Server.vue') }
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
