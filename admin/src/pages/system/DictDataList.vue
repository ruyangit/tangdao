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
        <div class="row q-col-gutter-md">
          <div>
            <div class="my-page-header-subtitle">字典数据</div>
            <div
              class="q-mt-sm"
              v-show="dictTypeForm.dictName"
            >{{ `${dictTypeForm.dictName } [${dictTypeForm.dictType }]`}}</div>
          </div>
          <q-space />
          <div class="row wrap content-end">
            {{ dictTypeForm.remarks}}
          </div>
        </div>
      </div>
    </div>

    <div class="my-page-body">
      <div class="my-table">
        <div class="my-search">
          <div class="q-gutter-md row items-start">
            <q-input
              outlined
              dense
              v-model.trim="form.dictLabel"
              placeholder="请输入字典标签"
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
              label="新增数据"
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
                key="dictLabel"
                :props="props"
              >
                <a
                  class="text-primary"
                  href="javascript:;"
                  @click="onEdit(props.row)"
                >{{ props.row.dictLabel }}</a>
              </q-td>
              <q-td
                key="dictValue"
                :props="props"
              >
                {{ props.row.dictValue }}
              </q-td>
              <q-td
                key="dictType"
                :props="props"
              >
                {{ props.row.dictType }}
              </q-td>
              <q-td
                key="treeSort"
                :props="props"
              >
                {{ props.row.treeSort }}
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
              >{{ props.row.createDate }}</q-td>
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
                  v-biz-delete:refresh="{data:{ id: props.row.id }, url:'/dictData/delete'}"
                >删除</a>
              </q-td>
            </q-tr>
          </template>
        </q-table>
      </div>
    </div>
    <q-dialog v-model="dictDataEdit">
      <q-card style="max-width: 760px">
        <q-card-section class="row items-center q-pb-none">
          <div class="text-h6"> {{dictDataForm.id?'编辑':'新增'}}数据</div>
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
            style="height: 570px;"
            class="scroll"
          >
            <q-card-section class="row q-col-gutter-md">
              <div class="col-6">
                <label
                  for="dictLabel"
                  class="q-label required"
                >
                  <span>字典标签</span>
                  <!-- <q-icon
                    name="error_outline"
                    class="q-icon"
                  /> -->
                </label>
                <q-input
                  outlined
                  dense
                  no-error-icon
                  v-model.trim="dictDataForm.dictLabel"
                  placeholder="请输入字典标签"
                  :rules="[ val => val && val.length > 0 || '请设置字典标签']"
                  class="q-mt-sm"
                >
                </q-input>
              </div>
              <div class="col-6">
                <label
                  for="dictLabel"
                  class="q-label "
                >
                  <span>上级标签</span>
                </label>
                <q-category-modal
                  title="上级标签"
                  v-model="dictDataForm.pid"
                  :toggle.sync="toggle"
                  :url="`/dictData/treeData?dictType=${form.dictType}&excludeCode=${dictDataForm.id||''}`"
                  @finish="onFinish"
                />
                <q-field
                  outlined
                  dense
                  class="q-mt-sm"
                >
                  <template v-slot:append>
                    <q-btn
                      flat
                      dense
                      size="12px"
                      icon="search"
                      @click="toggle=true"
                    />
                  </template>
                  <template v-slot:control>
                    <div
                      class="self-center full-width no-outline"
                      tabindex="0"
                    >{{dictDataForm.pname||'请选择上级标签'}}</div>
                  </template>
                </q-field>
                <!-- <q-input
                  outlined
                  dense
                  no-error-icon
                  v-model.trim="dictDataForm.pname"
                  placeholder="请选择上级标签"
                  class="q-mt-sm"
                  readonly
                >
                </q-input> -->
              </div>
              <div class="col-12">
                <label
                  for="dictLabel"
                  class="q-label required"
                >
                  <span>字典键值</span>
                </label>
                <q-input
                  outlined
                  dense
                  no-error-icon
                  v-model.trim="dictDataForm.dictValue"
                  placeholder="请输入字典键值"
                  :rules="[ val => val && val.length > 0 || '请设置字典键值']"
                  class="q-mt-sm"
                >
                </q-input>
              </div>
              <div class="col-6">
                <label
                  for="treeSort"
                  class="q-label required"
                >
                  <span>排序</span>
                </label>
                <q-input
                  outlined
                  dense
                  no-error-icon
                  v-model.number="dictDataForm.treeSort"
                  type="number"
                  placeholder="请输入排序值"
                  :rules="[ val => val && val !==null & val !== '' || '请设置排序值']"
                  class="q-mt-sm"
                >
                </q-input>
              </div>
              <div class="col-6">
                <label
                  for="description"
                  class="q-label "
                >
                  <span>字典描述</span>
                </label>
                <q-input
                  outlined
                  dense
                  no-error-icon
                  v-model.trim="dictDataForm.description"
                  placeholder="请输入字典描述"
                  class="q-mt-sm"
                >
                </q-input>
              </div>
              <div class="col-6">
                <label
                  for="cssStyle"
                  class="q-label "
                >
                  <span>CSS样式</span>
                </label>
                <q-input
                  outlined
                  dense
                  no-error-icon
                  v-model.trim="dictDataForm.cssStyle"
                  placeholder="请输入CSS样式"
                  class="q-mt-sm"
                >
                </q-input>
              </div>

              <div class="col-6">
                <label
                  for="cssClass"
                  class="q-label "
                >
                  <span>CSS类名</span>
                </label>
                <q-input
                  outlined
                  dense
                  no-error-icon
                  v-model.trim="dictDataForm.cssClass"
                  placeholder="请输入CSS类名"
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
                    v-model="dictDataForm.status"
                    color="primary"
                    :options="[
                      {label: '正常', value: '0'},
                      {label: '无效', value: '2'}
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
                  v-model="dictDataForm.remarks"
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
  name: 'DictDataList',
  meta: { title: '字典数据' },
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
        { name: 'dictLabel', label: '字典标签', align: 'left', field: 'dictLabel', style: 'width: 150px' },
        { name: 'dictValue', label: '字典键值', align: 'left', field: 'dictValue', style: 'width: 300px' },
        { name: 'dictType', label: '字典类型', align: 'left', field: 'dictType', style: 'width: 200px' },
        { name: 'treeSort', label: '排序', align: 'left', field: 'treeSort', style: 'width: 100px' },
        { name: 'remarks', label: '备注', align: 'left', field: 'remarks' },
        { name: 'status', label: '状态', field: 'status', align: 'left', sortable: true, style: 'width: 80px' },
        { name: 'createDate', label: '创建时间', field: 'createDate', align: 'left', sortable: true, style: 'width: 120px' },
        { name: 'action', label: '操作', align: 'center', field: 'action', style: 'width: 180px' }
      ],
      data: [],
      form: {
        dictName: null
      },
      dictTypeForm: {},
      dictDataEdit: false,
      dictDataForm: {
        treeSort: 10,
        status: '0'
      },
      toggle: false
    }
  },
  mounted () {
    this.onRefresh()
    this.onGetDictType(this.$route.params.dictType)
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
        url: '/dictData/page',
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
    async onGetDictType (value) {
      this.loading = true
      this.form.dictType = value
      await this.$fetchData({
        url: '/dictType?column=dict_type',
        method: 'GET',
        params: { value }
      }).then(response => {
        const { result, data } = response.data
        if (result && data) {
          this.dictTypeForm = {
            id: data.id,
            dictName: data.dictName,
            dictType: data.dictType,
            status: data.status,
            remarks: data.remarks
          }
        }
      }).catch(error => {
        console.error(error)
      })
      setTimeout(() => {
        this.loading = false
      }, 1000)
    },
    onAdd () {
      this.dictDataForm = this.$options.data().dictDataForm
      this.dictDataForm.dictType = this.dictTypeForm.dictType
      this.dictDataEdit = true
    },
    onEdit (data) {
      if (data) {
        const tns = data.treeNames.split('/')
        data.pname = tns[tns.length - 2]
      }
      this.dictDataForm = data
      delete this.dictDataForm.createBy
      delete this.dictDataForm.createDate
      delete this.dictDataForm.updateBy
      delete this.dictDataForm.updateDate
      this.dictDataEdit = true
    },
    onFinish ({ selected, selectedOptions }) {
      // console.log(selected)
      // console.log(selectedOptions)
      this.dictDataForm.pid = selected
      this.dictDataForm.pname = selectedOptions.label
    },
    async onSubmit () {
      this.loading = true
      await this.$fetchData({
        url: '/dictData/save',
        data: this.dictDataForm
      }).then(response => {
        const { result, message } = response.data
        if (result) {
          // 初始化
          this.dictDataForm = this.$options.data().dictDataForm

          this.$q.notify({ type: 'positive', message })
          this.dictDataEdit = false
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
