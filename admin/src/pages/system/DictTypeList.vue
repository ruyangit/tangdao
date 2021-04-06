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
          <q-breadcrumbs-el label="字典管理" />
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
              v-model.trim="form.dictName"
              placeholder="请输入字典名称"
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
              label="字典状态"
              style="width: 150px"
            />
            <q-btn
              color="primary"
              label="查询"
              class="btn wd-80"
              @click="onRefresh"
            />
            <q-space />
            <q-btn
              outline
              color="primary"
              label="新增字典"
              class="btn wd-90"
              @click="onAdd"
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
                key="dictName"
                :props="props"
              >
                <a
                  class="text-primary"
                  href="javascript:;"
                  @click="onEdit(props.row)"
                >{{ props.row.dictName|| '-' }}</a>
              </q-td>
              <q-td
                key="dictType"
                :props="props"
                class="text--line2-f"
              >
                <router-link
                  class="text-primary"
                  :to="`/sys/dictType/dictData/${props.row.dictType}`"
                >
                  {{ props.row.dictType }}
                </router-link>
              </q-td>
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
                <a
                  class="text-primary"
                  href="javascript:;"
                  @click="onEdit(props.row)"
                >编辑</a>
                <a
                  class="text-primary"
                  href="javascript:;"
                  v-biz-delete:refresh="{data:{ id: props.row.id }, url:'/dictType/delete'}"
                >删除</a>
              </q-td>
            </q-tr>
          </template>
        </q-table>
      </div>
    </div>
    <q-dialog v-model="dictTypeEdit">
      <q-card style="width: 600px">
        <q-card-section class="row items-center q-pb-none">
          <div class="text-h6"> {{dictTypeForm.id?'编辑':'新增'}}字典</div>
          <q-space />
          <q-btn
            icon="close"
            flat
            round
            dense
            v-close-popup
          />
        </q-card-section>
        <q-form
          class="my-form gutter"
          @submit="onSubmit"
        >
          <q-card-section
            style="height: 410px"
            class="scroll"
          >
            <q-card-section class="row q-col-gutter-md">
              <div class="col-12">
                <label
                  for="dictName"
                  class="q-label required"
                >
                  <span>字典名称</span>
                  <!-- <q-icon
                    name="error_outline"
                    class="q-icon"
                  /> -->
                </label>
                <q-input
                  outlined
                  dense
                  no-error-icon
                  v-model.trim="dictTypeForm.dictName"
                  placeholder="请输入字典名称"
                  :rules="[ val => val && val.length > 0 || '请设置字典名称']"
                  class="q-mt-sm"
                >
                </q-input>
              </div>
              <div class="col-12">
                <label
                  for="dictType"
                  class="q-label required"
                >字典类型</label>
                <q-input
                  outlined
                  dense
                  no-error-icon
                  v-model.trim="dictTypeForm.dictType"
                  placeholder="请输入字典类型"
                  :rules="[ val => val && val.length > 0 || '请设置字典类型']"
                  class="q-mt-sm"
                >
                </q-input>
              </div>
              <div class="col-12">
                <label
                  for="status"
                  class="q-label"
                >状态</label>
                <div class="q-mt-sm">
                  <q-option-group
                    inline
                    dense
                    v-model="dictTypeForm.status"
                    color="primary"
                    :options="[
                      {label: '正常', value: '0'},
                      {label: '禁用', value: '2'}
                    ]"
                  />
                </div>
              </div>
              <div class="col-12">
                <label for="remarks">备注</label>
                <q-input
                  dense
                  outlined
                  no-error-icon
                  v-model="dictTypeForm.remarks"
                  autogrow
                  :input-style="{ minHeight: '80px' }"
                  class="q-mt-sm"
                />
              </div>
            </q-card-section>

          </q-card-section>
          <q-separator />
          <q-card-actions align="right">
            <q-btn
              flat
              label="取消"
              color="primary"
              v-close-popup
              class="wd-80"
            />
            <q-btn
              label="保存"
              color="primary"
              type="submit"
              class="wd-80"
            />
          </q-card-actions>
        </q-form>
        <q-inner-loading :showing="loading">
          <q-spinner-hourglass
            size="sm"
            color="primary"
          />
        </q-inner-loading>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script>
export default {
  name: 'DictTypeList',
  meta: { title: '字典管理' },
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
        { name: 'dictName', label: '字典名称', align: 'left', field: 'dictName', style: 'width: 120px' },
        { name: 'dictType', label: '字典类型', align: 'left', field: 'dictType', style: 'width: 100px' },
        { name: 'remarks', label: '备注', align: 'left', field: 'remarks' },
        { name: 'status', label: '状态', field: 'status', align: 'left', sortable: true, style: 'width: 80px' },
        { name: 'createDate', label: '创建时间', field: 'createDate', align: 'left', sortable: true, style: 'width: 120px' },
        { name: 'action', label: '操作', align: 'center', field: 'action', style: 'width: 180px' }
      ],
      data: [],
      form: {
        dictName: null
      },
      dictTypeEdit: false,
      dictTypeForm: {
        status: '0',
        oldDictType: null
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
        pagination: this.pagination
      })
    },
    async onRequest (props) {
      const { page, rowsPerPage, sortBy, descending } = props.pagination
      this.loading = true
      this.form.current = page
      this.form.size = rowsPerPage
      await this.$fetchData({
        url: '/dictType/page',
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
    },
    onAdd () {
      this.dictTypeForm = this.$options.data().dictTypeForm
      this.dictTypeEdit = true
    },
    onEdit (data) {
      this.dictTypeForm = {
        id: data.id,
        dictName: data.dictName,
        dictType: data.dictType,
        oldDictType: data.dictType,
        status: data.status,
        remarks: data.remarks
      }
      this.dictTypeEdit = true
    },
    async onSubmit () {
      this.loading = true
      await this.$fetchData({
        url: '/dictType/save',
        data: this.dictTypeForm
      }).then(response => {
        const { result, message } = response.data
        if (result) {
          // 初始化
          this.dictTypeForm = this.$options.data().dictTypeForm

          this.$q.notify({ type: 'positive', message })
          this.dictTypeEdit = false
          this.onRefresh()
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
