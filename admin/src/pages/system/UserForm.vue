<template>
  <q-page class="my-page basic-form">
    <div class="row items-center justify-between">
      <div class="my-page-header">
        <q-breadcrumbs align="left">
          <q-breadcrumbs-el
            label="首页"
            to="/"
          />
          <q-breadcrumbs-el
            label="系统管理"
            to="/sys"
          />
          <q-breadcrumbs-el
            label="用户列表"
            to="/sys/user"
          />
          <q-breadcrumbs-el label="编辑用户" />
        </q-breadcrumbs>
        <div class="row q-col-gutter-md">
          <div>
            <div class="my-page-header-subtitle">用户表单</div>
            <div class="q-mt-sm">主要编辑用户信息及角色授权功能场景。</div>
          </div>
          <q-space />
          <div class="row wrap content-end">
            <q-btn-toggle
              v-model="form.mgrType"
              toggle-color="primary"
              :options="[
                  {label: '普通用户', value: '1'},
                  {label: '管理员', value: '2'}
                ]"
            />
          </div>
        </div>
      </div>
    </div>
    <div class="my-page-body">
      <q-form
        class="my-form gutter"
        @submit="onSubmit"
      >
        <q-card
          flat
          class="fit"
        >
          <q-card-section>用户信息</q-card-section>
          <q-separator />
          <q-card-section class="row q-col-gutter-md ">
            <div class="col-12 col-sm-6 col-lg-3">
              <label for="name">登录账号</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.username"
                placeholder="请输入登录账号"
                :rules="[ val => val && val.length > 0 || '请设置登录账号']"
                class="q-mt-sm"
              >
              </q-input>
            </div>
            <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
              <label for="nickname">用户昵称</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.nickname"
                placeholder="请输入用户昵称"
                class="q-mt-sm"
              >
              </q-input>
            </div>
            <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
              <label for="password">登录密码</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.password"
                placeholder="请输入登录密码"
                class="q-mt-sm"
              >
              </q-input>
            </div>
            <div class="col-12 col-sm-6 col-lg-3">
              <label for="phone">联系方式</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.phone"
                placeholder="请输入联系方式"
                class="q-mt-sm"
              >
              </q-input>
            </div>
            <div class="col-12 col-sm-12 col-lg-7 offset-lg-1">
              <label for="address">联系地址</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.address"
                placeholder="请输入联系地址"
                class="q-mt-sm"
              >
                <template v-slot:append>
                  <q-icon name="place" />
                </template>
              </q-input>
            </div>

            <div class="col-12 col-sm-6 col-lg-3">
              <label for="userType">用户类型</label>
              <div class="q-mt-sm">
                <q-btn-toggle
                  v-model="form.userType"
                  toggle-color="primary"
                  :options="[
                  {label: '个人', value: '1'},
                  {label: '企业', value: '2'}
                ]"
                />
              </div>
            </div>

          </q-card-section>
        </q-card>
        <q-card
          flat
          class="fit q-mt-md"
        >
          <q-card-section>详细信息</q-card-section>
          <q-separator />
          <q-card-section class="row q-col-gutter-md ">
            <div class="col-12 col-sm-6 col-lg-3">
              <label for="companyName">公司名称</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.companyName"
                placeholder="请输入公司名称"
                class="q-mt-sm"
              >
              </q-input>
            </div>
            <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
              <label for="companyCode">公司代码</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.companyCode"
                placeholder="请输入公司代码"
                class="q-mt-sm"
              >
              </q-input>
            </div>
            <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
              <label for="zipCode">邮政编码</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.zipCode"
                placeholder="请输入邮政编码"
                class="q-mt-sm"
              >
              </q-input>
            </div>
            <div class="col-12 col-sm-6 col-lg-3">
              <label for="wechat">微信</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.wechat"
                placeholder="请输入微信号"
                class="q-mt-sm"
              >
              </q-input>
            </div>
            <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
              <label for="dingding">钉钉</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.dingding"
                placeholder="请输入钉钉号"
                class="q-mt-sm"
              >
              </q-input>
            </div>
            <div class="col-12 col-lg-11">
              <label for="remarks">备注</label>
              <q-input
                dense
                outlined
                no-error-icon
                v-model="form.remarks"
                autogrow
                :input-style="{ minHeight: '60px' }"
                class="q-mt-sm"
              />
            </div>
          </q-card-section>
        </q-card>
        <q-card
          flat
          class="fit q-mt-md"
        >
          <q-card-section>分配角色</q-card-section>
          <q-separator />
          <q-card-section class="row">
            <div class="col-12 col-lg-11 my-table">
              <q-table
                :data="data"
                :columns="columns"
                row-key="id"
                selection="multiple"
                :selected.sync="selected"
                :pagination.sync="pagination"
                :loading="loading"
                @request="onRequest"
                binary-state-sort
                square
                class="q-table__card-f"
              >
                <template v-slot:no-data="{ message }">
                  <div class="full-width row flex-center q-gutter-sm q-pa-lg">
                    <span>
                      {{ message }}
                    </span>
                  </div>
                </template>
              </q-table>
            </div>
          </q-card-section>
        </q-card>
        <q-card
          flat
          class="fit q-mt-md"
        >
          <q-card-actions>
            <q-btn
              color="primary"
              class="wd-80"
              type="submit"
            >保存</q-btn>
            <q-btn
              class="wd-80"
              type="reset"
            >重置</q-btn>

            <q-space />

            <q-btn
              color="negative"
              class="wd-80"
              v-if="form.id"
              v-del:goback="{id:form.id, url:'/sys/user/delete'}"
            >删除</q-btn>

          </q-card-actions>
          <q-inner-loading :showing="loading">
            <q-spinner-hourglass
              size="sm"
              color="primary"
            />
          </q-inner-loading>
        </q-card>
      </q-form>
    </div>
  </q-page>
</template>

<script>
export default {
  name: 'UserForm',
  meta: { title: '用户表单' },
  data () {
    return {
      loading: false,
      pagination: {
        sortBy: null,
        descending: false,
        page: 1,
        rowsPerPage: 10,
        rowsNumber: 10
      },
      columns: [
        { name: 'roleCode', label: '角色代码', align: 'left', field: 'roleCode', style: 'width: 120px' },
        { name: 'roleName', label: '角色名称', align: 'left', field: 'roleName', style: 'width: 200px' },
        { name: 'remarks', label: '备注', align: 'left', field: 'remarks' }
      ],
      data: [],
      selected: [],
      form: {
        userType: '1',
        mgrType: '1',
        userIds: []
      }
    }
  },
  mounted () {
    this.onRefresh()
  },
  methods: {
    onRefresh () {
      this.pagination.page = 0
      this.onRequest({
        pagination: this.pagination,
        filter: null
      })
    },
    async onRequest (props) {
      const { page, rowsPerPage, sortBy, descending } = props.pagination
      this.loading = true
      await this.$fetchData({
        url: '/role/page',
        method: 'GET',
        params: { current: page, size: rowsPerPage, status: '0' }
      }).then(response => {
        const { result, data } = response.data
        if (result && data) {
          this.pagination.page = data.current
          this.pagination.rowsNumber = data.total
          this.pagination.rowsPerPage = data.size

          this.pagination.sortBy = sortBy
          this.pagination.descending = descending
          this.data = data.records
        }
      }).catch(error => {
        console.error(error)
      })
      setTimeout(() => {
        this.loading = false
      }, 1000)
    },
    async onSubmit () {
      this.loading = true
      this.form.userIds = this.selected ? this.selected.map(item => item.id) : []
      await this.$fetchData({
        url: '/user/save',
        data: this.form
      }).then(response => {
        const { result, message } = response.data
        if (result) {
          this.$q.notify({ type: 'positive', message })
          this.$router.go(-1)
        } else {
          this.$q.notify({ message })
        }
      }).catch(error => {
        console.error(error)
      })
      setTimeout(() => {
        this.loading = false
      }, 1000)
    }
  }
}
</script>

<style lang="sass" scoped>
</style>
