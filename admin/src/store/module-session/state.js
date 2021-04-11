
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
        id: 'overview',
        name: '分析页',
        icon: 'broken_image',
        path: 'overview'
      },
      {
        id: 'sys',
        name: '系统管理',
        icon: 'settings_applications',
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
            id: 'dictType',
            name: '字典管理',
            path: 'dictType'
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
        icon: 'insert_chart',
        path: 'sys/job'
      },
      {
        id: 'log',
        name: '日志监控',
        icon: 'casino',
        path: 'sys/log'
      },
      {
        id: 'server',
        name: '服务器监控',
        icon: 'stairs',
        path: 'sys/server'
      },
      {
        id: 'icons',
        name: '字体图标',
        icon: 'font_download',
        path: 'icons'
      }
    ]
  }
}
