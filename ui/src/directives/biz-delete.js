export default {
  name: 'biz-delete',
  bind (el, binding, vnode) {
    const ctx = {
      handler () {
        if (vnode.context && binding.value.url) {
          const _that = vnode.context
          _that.$q.dialog({
            title: '操作提示',
            message: '确定要删除吗？',
            cancel: true
          }).onOk(() => {
            vnode.context.loading = true
            _that.$fetchData({
              url: binding.value.url,
              data: binding.value.data
            }).then(response => {
              vnode.context.loading = false
              const { code, message, data } = response.data
              if (code === 0 && data) {
                _that.$q.notify({
                  type: 'positive',
                  message: '删除成功'
                })
                if (binding.arg === 'refresh' && vnode.context.onRefresh) {
                  vnode.context.onRefresh()
                } else if (binding.arg === 'goback') {
                  vnode.context.$router.go(-1)
                }
              } else {
                _that.$q.notify({
                  message
                })
              }
            }).catch(error => {
              vnode.context.loading = false
              console.log(error)
            })
          })
        }
      }
    }

    if (el.__qbiz_deleteclick !== undefined) {
      el.__qbiz_deleteclick_old = el.__qbiz_deleteclick
    }

    el.__qbiz_deleteclick = ctx

    el.addEventListener('click', ctx.handler)
  },

  // update (el, { value, oldValue }) {
  // },

  unbind (el) {
    const ctx = el.__qbiz_deleteclick_old || el.__qbiz_deleteclick
    if (ctx !== undefined) {
      el.removeEventListener('click', ctx.handler)
      delete el[el.__qbiz_deleteclick_old ? '__qbiz_deleteclick_old' : '__qbiz_deleteclick']
    }
  }
}
