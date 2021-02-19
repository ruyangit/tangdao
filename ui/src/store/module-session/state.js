
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
    icon: 'security',
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
        path: '/system/role'
      },
      {
        id: 'admin',
        name: '管理员',
        path: '/system/dashboard/workplace'
      }
    ]
  },
  {
    id: 'setting',
    name: '系统设置',
    icon: 'settings',
    path: '/system/setting',
    opened: true,
    children: [
      {
        id: 'account-center',
        name: '菜单管理',
        path: '/system/menu'
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
