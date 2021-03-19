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
            label="系统管理"
            to="/sys"
          />
          <q-breadcrumbs-el label="用户列表" />
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
            <div class="col-12 col-sm-2 col-lg-2 offset-lg-1">
              <label for="">状态</label>
              <q-input
                outlined
                dense
                v-model.trim="form.status"
              >
              </q-input>
            </div>
            <div class="col-12 col-sm-2 col-lg-2">
              <q-btn label="查询" />
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
                key="username"
                :props="props"
              >
                <router-link
                  :to="`user/form/${props.row.id}`"
                  class="text-primary"
                >{{ props.row.username|| '-' }}</router-link>
              </q-td>
              <q-td
                key="nickname"
                :props="props"
                class="text--line2-f"
              >{{ props.row.nickname }}</q-td>
              <q-td
                key="userType"
                :props="props"
              >{{ props.row.userType }}</q-td>
              <q-td
                key="roles"
                :props="props"
              >{{ '--' }}</q-td>
              <q-td
                key="phone"
                :props="props"
              >{{ '--' }}</q-td>
              <q-td
                key="lastLoginIp"
                :props="props"
              >{{ '--' }}</q-td>
              <q-td
                key="remarks"
                :props="props"
              >{{ '--' }}</q-td>
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
                  :to="`user/form/${props.row.id}`"
                  class="text-primary"
                >编辑</router-link>
                <a
                  class="text-primary"
                  href="javascript:;"
                  v-biz-delete:refresh="{data:{ id: props.row.id }, url:'/sys/user/delete'}"
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
        { name: 'username', label: '登录账号', align: 'left', field: 'username', style: 'width: 120px' },
        { name: 'nickname', label: '用户昵称', align: 'left', field: 'nickname', style: 'width: 100px' },
        { name: 'userType', label: '用户类型', align: 'center', field: 'userType', style: 'width: 100px' },
        { name: 'roles', label: '角色', field: 'roles', style: 'width: 150px' },
        { name: 'phone', label: '办公电话', field: 'phone', style: 'width: 120px' },
        { name: 'lastLoginIp', label: '最后登录时间', field: 'lastLoginIp', style: 'width: 200px' },
        { name: 'remarks', label: '备注', align: 'left', field: 'remarks' },
        { name: 'status', label: '状态', field: 'status', align: 'left', sortable: true, style: 'width: 80px' },
        { name: 'createDate', label: '创建时间', field: 'createDate', sortable: true, style: 'width: 120px' },
        { name: 'action', label: '操作', align: 'center', field: 'action', style: 'width: 180px' }
      ],
      user: {},
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
        if (code === '0' && data) {
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
