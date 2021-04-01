
export default function () {
  return {
    access_token: null,
    reset: {
      login: false,
      code: null,
      message: null
    },
    menus: [
      {
        id: 'sys',
        name: '系统管理',
        icon: 'settings',
        path: 'sys',
        opened: true,
        children: [
          {
            id: 'user',
            name: '用户列表',
            icon: 'account_box',
            path: 'user'
          },
          {
            id: 'role',
            name: '角色列表',
            icon: 'security',
            path: 'role'
          },
          {
            id: 'dict',
            name: '字典管理',
            path: 'dict'
          },
          {
            id: 'config',
            name: '配置列表',
            path: 'config'
          }
        ]
      },
      {
        id: 'job',
        name: '调度管理',
        icon: 'insert_chart_outlined',
        path: 'sys/job'
      },
      {
        id: 'log',
        name: '日志监控',
        icon: 'library_books',
        path: 'sys/log'
      }
    ]
  }
}
