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
          <q-breadcrumbs-el label="菜单管理" />
        </q-breadcrumbs>
        <div class="my-page-header-subtitle">菜单资源管理</div>
        <div class="q-mt-sm">高级表单常见于一次性输入和提交大批量数据的场景。</div>
      </div>
    </div>
    <div class="my-page-body">
      <div class="row q-col-gutter-md">
        <div class="col-12 col-sm-4 col-lg-4">
          <q-card
            flat
            class="fit"
          >
            <q-card-section class="q-pa-sm">
              <q-btn-dropdown
                outline
                color="primary"
                label="添加菜单"
              >
                <q-list dense>
                  <q-item
                    clickable
                    v-close-popup
                  >
                    <q-item-section>
                      <q-item-label>添加顶部菜单</q-item-label>
                    </q-item-section>
                  </q-item>
                  <q-item
                    clickable
                    v-close-popup
                  >
                    <q-item-section>
                      <q-item-label>添加子菜单</q-item-label>
                    </q-item-section>
                  </q-item>
                </q-list>
              </q-btn-dropdown>
              <q-btn
                class="q-ml-xs"
                outline
                color="negative"
                label="删除"
                disable
              />
            </q-card-section>
            <q-separator />
            <q-card-section class="q-pa-sm">
              <q-input
                dense
                label="请输入菜单名称搜索"
              >
                <template v-slot:append>
                  <q-icon name="search" />
                </template>
              </q-input>
            </q-card-section>
            <q-scroll-area
              class="fit"
              style="background:#fff;"
            >
              <q-tree
                class="q-mt-sm"
                :nodes="treeData"
                :ticked.sync="ticked"
                node-key="menuCode"
                label-key="menuName"
                tick-strategy="leaf-filtered"
                default-expand-all
                no-connectors
              />
            </q-scroll-area>
          </q-card>
        </div>
        <div class="col-12 col-sm-8 col-lg-8">
          <q-card
            flat
            class="fit"
          >
            <q-card-section>
              <q-icon
                name="content_paste"
                size="18px"
              /> 编辑菜单
            </q-card-section>
            <q-separator />
            <q-form
              class="my-form"
              @submit="onSubmit"
            >
              <q-card-section class="row q-col-gutter-md">
                <div class="col-12 col-md-6 col-lg-4">
                  <label for="name">菜单名称</label>
                  <q-input
                    outlined
                    dense
                    no-error-icon
                    v-model.trim="form.name"
                    placeholder="请输入菜单名称"
                    :rules="[ val => val && val.length > 0 || '请设置菜单名称']"
                    class="q-mt-sm"
                  >
                  </q-input>
                </div>
              </q-card-section>
              <q-card-actions class="q-pa-md">
                <q-btn
                  color="primary"
                  class="wd-80"
                  type="submit"
                >保存</q-btn>
                <q-btn
                  outline
                  color="primary"
                  class="wd-80"
                  type="reset"
                >重置</q-btn>
              </q-card-actions>
            </q-form>
          </q-card>

        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
  name: 'Menu',
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
    this.onRefresh()
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
