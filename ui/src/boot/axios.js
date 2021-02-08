import Axios from 'axios'
import { Notify } from 'quasar'
export default ({ router, Vue }) => {
  /**
   * axios 初始化
   */
  const axios = Axios.create({
    baseURL: process.env.API_HOST, // 请求基地址
    timeout: 60000 // 超时时间
  })

  // 请求拦截器
  axios.interceptors.request.use(
    config => {
      const token = sessionStorage.getItem('access_token')
      if (token && config.type) {
        config.headers.Authorization = 'Bearer ' + token
        switch (config.type) {
          case 'FORM-DATA':
            config.transformRequest = [data => { return 'args=' + JSON.stringify(data) }]
            break
          default:
            break
        }
      }
      return config
    },
    error => {
      return Promise.reject(error)
    }
  )

  // 响应拦截器
  axios.interceptors.response.use(
    response => {
      return response
    },
    error => {
      const defaultNotify = {
        message: '未知错误',
        timeout: 1500
      }
      if (error.toString() === 'Error: Network Error' || (error.code === 'ECONNABORTED' && error.message.indexOf('timeout') !== -1)) {
        defaultNotify.message = '网络异常'
        Notify.create(defaultNotify)
      } else {
        switch (error.response.status) {
          case 403:
            defaultNotify.message = '拒绝访问(403)'
            Notify.create(defaultNotify)
            break
          case 404:
            defaultNotify.message = '资源不存在(404)'
            Notify.create(defaultNotify)
            break
          case 408:
            defaultNotify.message = '请求超时(404)'
            Notify.create(defaultNotify)
            break
          case 500:
            defaultNotify.message = '服务器错误(500)'
            Notify.create(defaultNotify)
            break
          case 501:
            defaultNotify.message = '服务未实现(501)'
            Notify.create(defaultNotify)
            break
          case 502:
            defaultNotify.message = '网络错误(502)'
            Notify.create(defaultNotify)
            break
          case 503:
            defaultNotify.message = '服务不可用(503)'
            Notify.create(defaultNotify)
            break
          case 504:
            defaultNotify.message = '网络超时(504)'
            Notify.create(defaultNotify)
            break
          case 505:
            defaultNotify.message = 'HTTP版本不受支持(505)'
            Notify.create(defaultNotify)
            break
        }
      }
      return Promise.reject(error)
    }
  )

  /**
 * 自定义通用 axios 封装类
 * @param query 请求体
 * @returns {*}
 * @author ths
 */

  const fetchData = query => {
    console.warn(query)
    return axios({
      url: query.url, // 请求地址
      method: query.method || 'POST', // 请求方式，默认为 POST
      params: query.params, // 请求参数
      responseType: query.responseType || 'json', // 响应类型，默认为json
      auth: query.auth || { username: localStorage.getItem('access_token') },
      data: query.data || '' // 请求体数据 （仅仅post可用）
    })
  }

  Vue.prototype.$fetchData = fetchData
  Vue.prototype.$axios = axios
}
