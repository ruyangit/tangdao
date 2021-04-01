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
          <q-breadcrumbs-el label="角色列表" />
        </q-breadcrumbs>
      </div>
    </div>

    <div class="my-page-body">
      <div class="my-table">
        <div class="my-search">
          <div class="q-gutter-md row items-start">
            <q-input
              outlined
              dense
              v-model.trim="form.roleName"
              placeholder="请输入角色名称"
              style="width: 250px"
            >
            </q-input>
            <q-select
              outlined
              dense
              emit-value
              map-options
              options-dense
              v-model="form.status"
              :options="[{label: '正常', value: 0 },{label: '无效', value: 2}]"
              label="角色状态"
              style="width: 150px"
            />
            <q-btn
              color="primary"
              label="查询"
              class="btn wd-80"
            />
            <q-space />
            <q-btn
              outline
              color="primary"
              label="新增角色"
              class="btn wd-90"
            />
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
                key="roleCode"
                :props="props"
              >
                <router-link
                  :to="`role/form/${props.row.id}`"
                  class="text-primary"
                >{{ props.row.roleCode|| '-' }}</router-link>
              </q-td>
              <q-td
                key="roleName"
                :props="props"
                class="text--line2-f"
              >{{ props.row.roleName }}</q-td>
              <q-td
                key="remarks"
                :props="props"
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
  name: 'RoleList',
  meta: { title: '角色列表' },
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
        { name: 'remarks', label: '备注', align: 'left', field: 'remarks' },
        { name: 'status', label: '状态', field: 'status', align: 'left', sortable: true, style: 'width: 100px' },
        { name: 'createDate', label: '创建时间', field: 'createDate', align: 'left', sortable: true, style: 'width: 120px' },
        { name: 'action', label: '操作', align: 'center', field: 'action', style: 'width: 180px' }
      ],
      data: [],
      form: {
        roleName: null
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
        url: '/role/page',
        method: 'GET',
        params: { current: page, size: rowsPerPage, roleName: filter }
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
