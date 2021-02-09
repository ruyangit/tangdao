export default {
  name: 'biz-delete',
  bind (el, binding, vnode) {
    const ctx = {
      handler () {
        if (vnode.context && binding.value.url) {
          const _that = vnode.context
          _that.$q.dialog({
            title: vnode.context.$t('dialog.delete.title'),
            message: vnode.context.$t('dialog.delete.message'),
            cancel: true
          }).onOk(() => {
            vnode.context.loading = true
            const url = binding.value.url
            const form = binding.value

            axios.post(url, form).then(response => {
              vnode.context.loading = false
              const { code, message, data } = response.data
              if (code === '200' && data) {
                _that.$q.notify({
                  type: 'positive',
                  message: vnode.context.$t('dialog.delete.success')
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

    if (el.__qcomdelclick !== void 0) {
      el.__qcomdelclick_old = el.__qcomdelclick
    }

    el.__qcomdelclick = ctx

    el.addEventListener('click', ctx.handler)
  },

  // update (el, { value, oldValue }) {
  // },

  unbind (el) {
    const ctx = el.__qcomdelclick_old || el.__qcomdelclick
    if (ctx !== void 0) {
      el.removeEventListener('click', ctx.handler)
      delete el[el.__qcomdelclick_old ? '__qcomdelclick_old' : '__qcomdelclick']
    }
  }
}
