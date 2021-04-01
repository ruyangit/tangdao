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
          <q-breadcrumbs-el label="配置列表" />
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
              v-model.trim="form.configName"
              placeholder="请输入配置名称"
              style="width: 250px"
            >
            </q-input>
            <q-input
              outlined
              dense
              v-model.trim="form.configKey"
              placeholder="请输入配置键"
              style="width: 250px"
            >
            </q-input>

            <q-btn
              color="primary"
              label="查询"
              class="btn wd-80"
            />
            <q-space />
            <q-btn
              outline
              color="primary"
              label="新增配置"
              class="btn wd-90"
              to="/sys/config/form"
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
                key="configName"
                :props="props"
              >
                <router-link
                  :to="`role/form/${props.row.id}`"
                  class="text-primary"
                >{{ props.row.configName|| '-' }}</router-link>
              </q-td>
              <q-td
                key="configKey"
                :props="props"
                class="text--line2-f"
              >{{ props.row.configKey }}</q-td>
              <q-td
                key="configValue"
                :props="props"
              >{{ props.row.configValue }}</q-td>
              <q-td
                key="remarks"
                :props="props"
              >{{ props.row.remarks }}</q-td>
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
                  :to="`config/form/${props.row.id}`"
                  class="text-primary"
                >编辑</router-link>
                <a
                  class="text-primary"
                  href="javascript:;"
                  v-biz-delete:refresh="{data:{ id: props.row.id }, url:'/sys/config/delete'}"
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
  name: 'ConfigList',
  meta: { title: '配置列表' },
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
        { name: 'configName', label: '配置名称', align: 'left', field: 'configName', style: 'width: 260px' },
        { name: 'configKey', label: '配置键', align: 'left', field: 'configKey', style: 'width: 260px' },
        { name: 'configValue', label: '配置值', align: 'left', field: 'configValue', style: 'width: 200px' },
        { name: 'remarks', label: '备注', align: 'left', field: 'remarks' },
        { name: 'createDate', label: '创建时间', field: 'createDate', sortable: true, style: 'width: 120px' },
        { name: 'action', label: '操作', align: 'center', field: 'action', style: 'width: 180px' }
      ],
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
      this.loading = true
      this.form.current = page
      this.form.size = rowsPerPage
      await this.$fetchData({
        url: '/config/page',
        method: 'GET',
        params: this.form
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
