
const gettingComponent = [
  {
    id: 'overview',
    name: '概览',
    icon: 'broken_image',
    path: '/system/overview'
  },
  {
    id: 'user',
    name: '权限',
    icon: 'supervised_user_circle',
    path: '/system/user',
    children: [
      {
        id: 'account-center',
        name: '用户',
        path: '/component/18'
      },
      {
        id: 'role',
        name: '角色',
        icon: 'view_list',
        path: '/system/role'
      },
      {
        id: 'admin',
        name: '管理员',
        icon: 'widgets',
        path: '/system/dashboard/workplace'
      }
    ]
  },
  {
    id: 'setting',
    name: '系统设置',
    icon: 'library_books',
    path: '/system/setting',
    children: [
      {
        id: 'account-center',
        name: '菜单管理',
        path: '/component/18'
      },
      {
        id: 'account-seting',
        name: '系统配置',
        path: '/component/19'
      },
      {
        id: 'account-seting1',
        name: '字典管理',
        path: '/component/19'
      },
      {
        id: 'area',
        name: '行政区域',
        path: '/component/19'
      }
    ]
  },
  {
    id: 'log',
    name: '日志',
    icon: 'assignment',
    path: '/system/dashboard/workplace'
  }
]
export default function () {
  return {
    menus: [
      {
        id: 'dashboard',
        name: '总览',
        icon: 'eco',
        path: '/dashboard'
      },
      {
        id: 'system',
        name: '系统管理',
        icon: 'style',
        path: '/system',
        opened: true,
        children: gettingComponent
      }
    ]
  }
}
