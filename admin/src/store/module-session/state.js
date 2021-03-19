
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
        name: '概览',
        icon: 'broken_image',
        path: 'overview'
      },
      {
        id: 'sms',
        name: '短信',
        icon: 'layers',
        path: 'sms',
        opened: true,
        children: [
          {
            id: 'sms-record',
            name: '短信记录',
            path: 'record'
          },
          {
            id: 'sms-fail-record',
            name: '失败记录',
            path: 'fail-record'
          },
          {
            id: 'sms-reply-record',
            name: '回复记录',
            path: 'reply-record'
          },
          {
            id: 'sms-receipt-analysis',
            name: '回执分析',
            path: 'receipt-analysis'
          }
        ]
      },
      {
        id: 'setting',
        name: '设置',
        icon: 'settings',
        path: 'setting',
        children: [
          {
            id: 'setting-blacklist',
            name: '黑名单管理',
            path: 'blacklist'
          },
          {
            id: 'setting-replyurl',
            name: '回调配置',
            path: 'replyurl'
          },
          {
            id: 'setting-account',
            name: '账户管理',
            path: 'account'
          }
        ]
      }
    ]
  }
}
