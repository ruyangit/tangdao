<template>
  <q-page class="my-page">
    <div class="row items-center justify-between">
      <div class="my-page-header q-pb-none">
        <q-breadcrumbs align="left">
          <q-breadcrumbs-el
            label="首页"
            to="/"
          />
          <q-breadcrumbs-el
            label="短信"
            to="/sms"
          />
          <q-breadcrumbs-el label="短信记录" />
        </q-breadcrumbs>
      </div>
    </div>

    <div class="my-page-body">
      <div class="my-table">
        <div class="my-search">
          <div class="row q-col-gutter-md">
            <div class="col-12 col-sm-3 col-lg-3">
              <label for="">登录账号</label>
              <q-input
                outlined
                dense
                v-model.trim="form.username"
                placeholder="请输入登录账号"
              >
              </q-input>
            </div>
            <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
              <label for="">状态</label>
              <q-input
                outlined
                dense
                v-model.trim="form.status"
              >
              </q-input>
            </div>
          </div>
        </div>
        <q-table
          :data="data"
          :columns="columns"
          row-key="id"
          :pagination.sync="pagination"
          :loading="loading"
          @request="onRequest"
          binary-state-sort
          square
        >
          <template v-slot:no-data="{ message }">
            <div class="full-width row flex-center q-gutter-sm q-pa-lg">
              <span>
                {{ message }}
              </span>
            </div>
          </template>

          <template v-slot:body="props">
            <q-tr :props="props">
              <q-td
                key="roleName"
                :props="props"
              >
                <router-link
                  :to="`role/form/${props.row.roleCode}`"
                  class="text-primary"
                >{{ props.row.roleName|| '-' }}</router-link>
              </q-td>
              <q-td
                key="remark"
                :props="props"
                class="text--line2-f"
              >{{ props.row.remarks }}</q-td>
              <q-td
                key="status"
                :props="props"
              >
                <q-status v-model="props.row.status" />
              </q-td>
              <q-td
                key="createDate"
                :props="props"
              >{{ props.row.createDate || '-' }}</q-td>
              <q-td
                key="action"
                :props="props"
                class="q-gutter-xs action"
              >
                <router-link
                  :to="`role/form/${props.row.roleCode}`"
                  class="text-primary"
                >编辑</router-link>
                <a
                  class="text-primary"
                  href="javascript:;"
                  v-biz-delete:refresh="{data:{ roleCode: props.row.roleCode }, url:'/v1/system/deleteRole'}"
                >删除</a>
              </q-td>
            </q-tr>
          </template>
        </q-table>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
  name: 'UserList',
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
        { name: 'username', label: '登录账号', field: 'username', style: 'width: 100px' },
        { name: 'nickname', label: '用户昵称', field: 'nickname', style: 'width: 100px' },
        { name: 'userType', label: '用户类型', field: 'userType' },
        { name: 'roles', label: '角色', field: 'roles', style: 'width: 100px' },
        { name: 'lastLoginIp', label: '最后登录时间', field: 'lastLoginIp', style: 'width: 100px' },
        { name: 'phone', label: '办公电话', field: 'phone', sortable: true, style: 'width: 100px' },
        { name: 'status', label: '状态', field: 'status', sortable: true, style: 'width: 100px' },
        { name: 'action', label: '操作', align: 'center', field: 'action', style: 'width: 180px' }
      ],
      role: {},
      data: [],
      form: {
        username: null
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
      const filter = props.filter
      this.loading = true
      await this.$fetchData({
        url: '/user/record',
        method: 'GET',
        params: { current: page, size: rowsPerPage, username: filter }
      }).then(response => {
        const { code, data } = response.data
        if (code === 0 && data) {
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
    }
  }
}
</script>
