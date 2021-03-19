
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
            path: 'user'
          },
          {
            id: 'role',
            name: '角色列表',
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
          },
          {
            id: 'job',
            name: '调度管理',
            path: 'job'
          },
          {
            id: 'log',
            name: '日志监控',
            path: 'log'
          }
        ]
      }
    ]
  }
}
