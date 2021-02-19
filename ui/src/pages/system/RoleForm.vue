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
            label="权限"
            to="/system"
          />
          <q-breadcrumbs-el label="角色" />
        </q-breadcrumbs>
      </div>
    </div>
    <div class="my-page-body">
      <div class="my-tabs">
        <q-tabs
          narrow-indicator
          align="left"
          class="text-grey"
          active-color="primary"
          indicator-color="primary"
        >
          <q-route-tab to="/system/role">角色列表</q-route-tab>
          <q-route-tab :to="`/system/role/form${form.roleCode!=null?'/'+form.roleCode:''}`">{{form.roleCode!=null?'编辑':'新增'}}角色</q-route-tab>
        </q-tabs>
      </div>
      <q-card
        flat
        class="fit "
      >
        <div class="container">
          <q-form
            class="my-form gutter"
            @submit="onSubmit"
          >
            <q-card-section class="q-pa-xl">
              <div class="row q-col-gutter-md">
                <div class="col-12 col-md-6 col-lg-4">
                  <label for="roleName"> 角色名称</label>
                  <q-input
                    outlined
                    dense
                    no-error-icon
                    v-model.trim="form.roleName"
                    placeholder="请输入角色名称"
                    :rules="[ val => val && val.length > 0 || '请设置角色名称']"
                    class="q-mt-sm"
                  >
                  </q-input>
                </div>
              </div>
              <div class="row q-col-gutter-md q-mt-xs">
                <div class="col-12 col-md-6 col-lg-4">
                  <label for="remark"> 角色描述 </label>
                  <div class="q-mt-sm">
                    <q-input
                      dense
                      outlined
                      no-error-icon
                      v-model="form.remarks"
                      autogrow
                      :input-style="{ minHeight: '60px' }"
                    />
                  </div>
                </div>
              </div>
              <div class="row q-col-gutter-md q-mt-xs">
                <div class="col-12">
                  <label for=""> 权限分配 </label>
                  <div
                    class="q-mt-sm"
                    style="max-width:300px"
                  >
                    <q-tree
                      :nodes="treeData"
                      node-key="menuCode"
                      label-key="menuName"
                      tick-strategy="leaf-filtered"
                      :ticked.sync="ticked"
                      :duration="0"
                      no-connectors
                    />
                  </div>
                </div>

              </div>
            </q-card-section>
            <q-separator />
            <q-card-actions class="q-pa-xl">
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
              <q-space />
              <q-btn
                color="negative"
                class="wd-80"
                v-if="form.roleCode"
                v-biz-delete:goback="{data:{ roleCode: form.roleCode }, url:'/v1/system/deleteRole'}"
              >删除</q-btn>
            </q-card-actions>
          </q-form>
        </div>
        <q-inner-loading :showing="loading">
          <q-spinner-hourglass
            size="sm"
            color="primary"
          />
        </q-inner-loading>
      </q-card>
    </div>
  </q-page>
</template>

<script>
export default {
  name: 'RoleForm',
  data () {
    return {
      loading: false,
      form: {
        roleCode: this.$route.params.id
      },
      oldRoleName: null,
      treeData: [],
      ticked: []
    }
  },
  mounted () {
    if (this.form.roleCode) {
      this.onMenuTree()
      this.onRequest()
    } else {
      this.onMenuTree()
    }
  },
  methods: {
    async onRequest () {
      this.loading = true
      await this.$fetchData({
        url: '/v1/system/getRole',
        method: 'GET',
        params: { roleCode: this.form.roleCode }
      }).then(response => {
        const { code, data } = response.data
        if (code === 0 && data) {
          this.form = data.role
          this.oldRoleName = this.form.roleName
          this.ticked = data.menuCodes
        }
      }).catch(error => {
        console.error(error)
      })
      setTimeout(() => {
        this.loading = false
      }, 200)
    },
    async onMenuTree () {
      await this.$fetchData({
        url: '/v1/system/queryMenuTreeData',
        method: 'GET'
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
      }, 200)
    },
    async onSubmit () {
      this.loading = true
      this.form.oldRoleName = this.oldRoleName
      this.form.menuCodes = this.ticked
      delete this.form.createDate
      delete this.form.status
      await this.$fetchData({
        url: '/v1/system/saveOrUpdateRole',
        data: this.form
      }).then(response => {
        const { code, message, data } = response.data
        if (code === 0 && data) {
          this.$q.notify({
            type: 'positive',
            message: '保存成功'
          })
          this.$router.go(-1)
        } else {
          this.$q.notify({
            message
          })
        }
      }).catch(error => {
        console.error(error)
      })
      setTimeout(() => {
        this.loading = false
      }, 200)
    }
  }
}
</script>
