<template>
  <q-page class="my-page">
    <div class="row items-center justify-between">
      <div class="my-page-header q-pb-none">
        <q-breadcrumbs align="left">
          <q-breadcrumbs-el
            label="首页"
            to="/"
          />
          <q-breadcrumbs-el label="日志监控" />
        </q-breadcrumbs>
        <div class="my-page-header-subtitle">{{$t('nativeName')}}</div>
      </div>
    </div>

    <div class="my-page-body">
      <div class="my-table">
        <div class="my-search">
          <div class="q-gutter-md row items-start">
            <q-input
              outlined
              dense
              v-model.trim="form.logTitle"
              placeholder="请输入日志标题"
              style="width: 250px"
            >
            </q-input>
            <q-select
              outlined
              dense
              emit-value
              map-options
              options-dense
              v-model="form.logType"
              :options="[
                {label: '接入日志', value: 'access' },
                {label: '修改日志', value: 'update'},
                {label: '查询日志', value: 'select'},
                {label: '登录登出', value: 'loginLogout'}
              ]"
              label="日志类型"
              style="width: 150px"
            />
            <q-btn
              color="primary"
              label="查询"
              class="btn wd-80"
              @click="onRefresh"
            />
            <q-space />

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
                auto-width
                key="expand"
              >
                <q-btn
                  size="xs"
                  round
                  dense
                  @click="props.expand = !props.expand"
                  :icon="props.expand ? 'remove' : 'add'"
                />
              </q-td>
              <q-td
                key="logTitle"
                :props="props"
              >
                {{ props.row.logTitle|| '-' }}
              </q-td>
              <q-td
                key="requestUri"
                :props="props"
                class="text--line2-f"
              >{{ props.row.requestUri }}</q-td>
              <q-td
                key="logType"
                :props="props"
              >{{ props.row.logType }}</q-td>
              <q-td
                key="createByName"
                :props="props"
              >{{ props.row.createByName }}</q-td>
              <q-td
                key="isException"
                :props="props"
              >{{ props.row.isException }}</q-td>
              <q-td
                key="createDate"
                :props="props"
              >{{ props.row.createDate }}</q-td>
              <q-td
                key="remoteAddr"
                :props="props"
              >{{ props.row.remoteAddr }}</q-td>
              <q-td
                key="deviceName"
                :props="props"
                class="text--line2-f"
              >{{ props.row.deviceName }}</q-td>
              <q-td
                key="browserName"
                :props="props"
                class="text--line2-f"
              >{{ props.row.browserName}}</q-td>
              <q-td
                key="executeTimeFormat"
                :props="props"
              >{{ props.row.executeTimeFormat}}</q-td>
            </q-tr>
            <q-tr
              v-show="props.expand"
              :props="props"
            >
              <q-td colspan="100%">
                <div class="text-left">This is expand slot for row above: {{ props.row.username }}.</div>
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
  name: 'LogList',
  meta: { title: '日志监控' },
  data () {
    return {
      loading: false,
      pagination: {
        sortBy: null,
        descending: false,
        page: 1,
        rowsPerPage: 20,
        rowsNumber: 20
      },
      columns: [
        { name: 'expand', field: 'expand' },
        { name: 'logTitle', label: '日志标题', align: 'left', field: 'logTitle', style: 'width: 120px' },
        { name: 'requestUri', label: '请求地址', align: 'left', field: 'requestUri', style: 'width: 100px' },
        { name: 'logType', label: '日志类型', align: 'center', field: 'logType', style: 'width: 100px' },
        { name: 'createByName', label: '操作用户', field: 'createByName', style: 'width: 100px' },
        { name: 'isException', label: '异常', field: 'isException', style: 'width: 60px' },
        { name: 'createDate', label: '操作时间', field: 'createDate', style: 'width: 120px' },
        { name: 'remoteAddr', label: '客户端', field: 'remoteAddr', style: 'width: 100px' },
        { name: 'deviceName', label: '设备名称', align: 'left', field: 'deviceName', style: 'width: 150px' },
        { name: 'browserName', label: '浏览器名称', field: 'browserName', align: 'left', style: 'width: 100px' },
        { name: 'executeTimeFormat', label: '执行耗时', field: 'executeTimeFormat', align: 'left', style: 'width: 80px' }
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
        pagination: this.pagination
      })
    },
    async onRequest (props) {
      const { page, rowsPerPage, sortBy, descending } = props.pagination
      this.loading = true
      this.form.current = page
      this.form.size = rowsPerPage
      await this.$fetchData({
        url: '/log/page',
        method: 'GET',
        params: this.form
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
    }
  }
}
</script>
