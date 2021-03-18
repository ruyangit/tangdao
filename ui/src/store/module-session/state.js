
const smsComponent = [
  {
    id: 'overview',
    name: '概览',
    icon: 'broken_image',
    path: '/sms/overview'
  },
  {
    id: 'list',
    name: '短信',
    icon: 'layers',
    opened: true,
    children: [
      {
        id: 'smsrecord',
        name: '短信记录',
        path: '/sms/record'
      },
      {
        id: 'smsfailrecord',
        name: '失败记录',
        path: '/sms/fail/record'
      },
      {
        id: 'smsrev',
        name: '回复记录',
        path: '/sms/rev/record'
      },
      {
        id: 'receipt-analysis',
        name: '回执分析',
        path: '/sms/receipt/analysis'
      }
    ]
  },
  {
    id: 'setting',
    name: '设置',
    icon: 'settings',
    children: [
      {
        id: 'blacklist',
        name: '黑名单管理',
        path: '/sms/blacklist'
      },
      {
        id: 'replyurl',
        name: '回调配置',
        path: '/sms/replyurl'
      },
      {
        id: 'account',
        name: '账户管理',
        path: '/user/record'
      }
    ]
  }
]
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
        id: 'sms',
        name: '短信',
        icon: 'style',
        path: '/sms',
        children: smsComponent
      }
    ]
  }
}
