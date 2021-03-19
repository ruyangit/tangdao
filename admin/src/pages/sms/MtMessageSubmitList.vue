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
            <div class="col-12 col-sm-6 col-lg-3">
              <label for="">消息ID</label>
              <q-input
                outlined
                dense
                v-model.trim="form.name"
                placeholder="请输入消息ID"
              >
              </q-input>
            </div>
            <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
              <label for="">手机号码</label>
              <q-input
                outlined
                dense
                v-model.trim="form.name"
                placeholder="请输入手机号码"
              >
              </q-input>
            </div>
            <div class="col-12 col-sm-12 col-lg-3 offset-lg-1">
              <label for="">消息内容</label>
              <q-input
                outlined
                dense
                v-model.trim="form.name"
                placeholder="请输入消息内容"
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
          :filter="roleName"
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
  name: 'RoleList',
  data () {
    return {
      loading: false,
      roleName: null,
      pagination: {
        sortBy: null,
        descending: false,
        page: 1,
        rowsPerPage: 10,
        rowsNumber: 10
      },
      columns: [
        { name: 'mobile', label: '手机号码', align: 'left', field: 'mobile', style: 'width: 100px' },
        { name: 'content', label: '消息内容', align: 'left', field: 'content' },
        { name: 'fee', label: '字数/拆分条数', align: 'right', field: 'fee', sortable: true, style: 'width: 100px' },
        { name: 'createTime', label: '发送时间', align: 'center', field: 'createTime', sortable: true, style: 'width: 100px' },
        { name: 'receiptTime', label: '回执时间', align: 'center', field: 'receiptTime', sortable: true, style: 'width: 100px' },
        { name: 'status', label: '状态', align: 'left', field: 'status', sortable: true, style: 'width: 100px' },
        { name: 'remarks', label: '备注', align: 'center', field: 'remarks', style: 'width: 180px' }
      ],
      role: {},
      data: [],
      form: {}
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
        url: '/sms/record',
        method: 'GET',
        params: { current: page, size: rowsPerPage, roleName: filter }
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
