<template>
  <q-page class='my-page basic-form'>
    <div class='row items-center justify-between'>
      <div class='my-page-header'>
        <q-breadcrumbs align='left'>
          <q-breadcrumbs-el
            label='首页'
            to='/'
          />
          <q-breadcrumbs-el label='字体图标' />
        </q-breadcrumbs>
        <div class="row q-col-gutter-md">
          <div>
            <div class='my-page-header-subtitle'>图标列表</div>
            <div class='q-mt-sm'>内置图标一览，图标选择功能场景。</div>
          </div>
          <q-space />
          <div class="row wrap content-end">
            <q-btn-toggle
              v-model="form.iconType"
              toggle-color="primary"
              :options="[
                  {label: 'Material Icons', value: 'material-icons'},
                ]"
            />
          </div>
        </div>
      </div>
    </div>
    <div class='my-page-body'>
      <q-form class='my-form gutter'>
        <q-card
          flat
          class='fit'
        >
          <q-card-section id="materialIcons"></q-card-section>
        </q-card>
      </q-form>
    </div>
  </q-page>
</template>

<script>
import * as materialIconsSet from '@quasar/extras/material-icons'
import { copyToClipboard } from 'quasar'
export default {
  name: 'Icons',
  meta: { title: '字体图标' },
  data () {
    return {
      loading: false,
      form: {
        iconType: 'material-icons'
      },
      materialIconsKey: []
    }
  },
  created () {
    window.onCopyToClipboard = this.onCopyToClipboard
  },
  mounted () {
    // console.log(materialIconsSet)
    this.onInit()
  },
  methods: {
    onInit () {
      for (const i in materialIconsSet) {
        this.materialIconsKey.push(this.toLowerLine(i))
      }
      this.$nextTick(() => {
        const fragment = document.createDocumentFragment()

        this.materialIconsKey.map(e => {
          const li = document.createElement('li')
          li.innerText = e

          li.setAttribute('class', 'my-icon material-icons q-icon notranslate')
          li.setAttribute('onclick', 'window.onCopyToClipboard(' + "'" + e + "'" + ')')
          fragment.appendChild(li)
        })

        document.getElementById('materialIcons').appendChild(fragment)
      })
    },
    toLowerLine (str) {
      if (str.substr(0, 3) === 'mat') {
        let t = str.replace(/([A-Z]|\d+)/g, (a, l) => `_${l.toLowerCase()}`).substring(4)
        switch (t) {
          case 'crop_32':
            t = 'crop_3_2'
            break
          case 'crop_169':
            t = 'crop_16_9'
            break
          case 'crop_54':
            t = 'crop_5_4'
            break
          case 'crop_75':
            t = 'crop_7_5'
            break
          default:
            break
        }
        return t
      }
    },
    onCopyToClipboard (e) {
      copyToClipboard(e).then(() => {
        this.$q.notify({
          message: '成功复制到剪切板 [' + e + ']',
          type: 'positive',
          position: 'top'
        })
      }).catch(error => {
        console.error(error)
      })
    }
  }
}
</script>

<style lang='sass'>
.my-icon
  padding: 15px
  font-size: 28px
  cursor: pointer
  &:hover
    background: $positive
    color: $light
</style>
