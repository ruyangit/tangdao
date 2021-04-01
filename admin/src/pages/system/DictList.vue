<template>
  <q-page class="my-page">
    <div class="row items-center justify-between">
      <div class="my-page-header">
        <q-breadcrumbs align="left">
          <q-breadcrumbs-el
            label="首页"
            to="/"
          />
          <q-breadcrumbs-el
            label="系统设置"
            to="/system"
          />
          <q-breadcrumbs-el label="字典管理" />
        </q-breadcrumbs>
        <div class="my-page-header-subtitle">字典资源管理</div>
        <div class="q-mt-sm">高级表单常见于一次性输入和提交大批量数据的场景。</div>
      </div>
    </div>
    <div class="my-page-body">
      <div class="row q-col-gutter-md">
        <div class="col-12 col-lg-3">
          <q-card
            flat
            class="fit"
          >
            <q-card-section class="q-pa-sm">
              <q-btn-dropdown
                outline
                color="primary"
                label="添加字典"
              >
                <q-list dense>
                  <q-item
                    clickable
                    v-close-popup
                  >
                    <q-item-section>
                      <q-item-label>新增字典分类</q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
              </q-btn-dropdown>
              <q-btn
                class="q-ml-xs wd-80"
                outline
                color="negative"
                label="删除"
                disable
              />
            </q-card-section>
            <q-separator />
            <q-card-section class="q-pa-none q-mb-lg">
              <q-list separator>
                <q-item-label
                  header
                  class="q-pa-none"
                >
                  <q-input
                    dense
                    label="请输入字典类型名称"
                  >
                    <template v-slot:append>
                      <q-icon name="search" />
                    </template>
                  </q-input>
                </q-item-label>
                <q-item
                  clickable
                  v-ripple
                >
                  <q-item-section>
                    <q-item-label>Content filtering</q-item-label>
                    <q-item-label caption>
                      Set the content filtering level to restrict
                      apps that can be downloaded
                    </q-item-label>
                  </q-item-section>
                </q-item>

                <q-item
                  clickable
                  v-ripple
                >
                  <q-item-section>
                    <q-item-label>Password</q-item-label>
                    <q-item-label caption>
                      Require password for purchase or use
                      password to restrict purchase
                    </q-item-label>
                  </q-item-section>
                </q-item>
              </q-list>
            </q-card-section>
          </q-card>
        </div>
        <div class="col-12 col-lg-9">
          <q-card
            flat
            class="fit"
          >
            <q-card-section class="row q-pa-sm">
              <q-btn
                flat
                icon="apps"
                label="字典列表"
              />
              <q-space />
              <q-btn
                outline
                color="primary"
                label="新增数据"
              />
            </q-card-section>
            <q-separator />

          </q-card>

        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
  name: 'DictList',
  meta: { title: '字典管理' },
  data () {
    return {
      loading: false,
      form: {
      },
      treeData: [],
      ticked: []
    }
  },
  mounted () {
    // this.onRefresh()
  },
  methods: {
    onRefresh () {
      this.onRequest()
    },
    async onRequest () {
      this.loading = true
      await this.$fetchData({
        url: '/v1/system/queryMenuTreeData',
        method: 'GET',
        params: {}
      }).then(response => {
        const { code, data } = response.data
        if (code === 0 && data) {
          this.treeData = data
        }
      }).catch(error => {
        console.error(error)
      })
      setTimeout(() => {
        this.loading = false
      }, 1000)
    },
    onSubmit () {

    }
  }
}
</script>

<style lang="sass" scoped>
</style>
