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
      <div class="my-table ">
        <div class="my-search">
          <div class="q-gutter-md row items-start">
            <div class="text-h6">DictData Record</div>
            <q-space />
            <q-btn
              label="展开"
              color="primary"
              @click="onExpand"
            />
            <q-btn
              label="折叠"
              color="primary"
              @click="onCollapse"
            />
            <q-btn
              label="刷新"
              color="primary"
              :loading="loading"
              @click="onRefresh"
            />
            <q-btn
              outline
              color="primary"
              label="新增数据"
              class="wd-90"
              @click="onAdd"
            />
          </div>
        </div>
        <q-markup-table>
          <thead>
            <tr>
              <th class="text-left wd-300">字典标签</th>
              <th class="text-left wd-250">字典键值</th>
              <th class="text-left wd-80">字典类型</th>
              <th class="text-left wd-80">排序</th>
              <th class="text-left">备注</th>
              <th class="text-left wd-150">状态</th>
              <th class="text-left wd-150">创建时间</th>
              <th class="text-left wd-150">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(item,index) in retData"
              :key="item.id"
              :id="item.id"
              v-show="item.show || true"
            >
              <td class="relative-position">
                <div
                  :style="`width:${item.level?item.level*20:20}px; text-align:right;`"
                  class="relative-position float-left"
                >

                  <q-spinner-oval
                    color="primary"
                    size="10px"
                    style="margin-right: 6px"
                    v-if="item.loading || false"
                  />
                  <template v-else>
                    <div
                      class="item-circle"
                      v-if="item.treeLeaf==='1'"
                    >
                    </div>
                    <q-icon
                      :id="`onc_${item.id}`"
                      :name="item.show?'arrow_drop_down':'arrow_right'"
                      size="20px"
                      color="primary"
                      class="cursor-pointer"
                      @click="expandRow(item, index)"
                      v-else
                    />
                  </template>
                </div>
                <span
                  class="absolute"
                  style="top:10px;"
                >{{item.dictLabel}}</span>
              </td>
              <td>{{item.dictValue}}</td>
              <td>{{item.dictType}}</td>
              <td>{{item.treeSort}}</td>
              <td>{{item.remarks}}</td>
              <td>
                <q-status v-model="item.status" />
              </td>
              <td>{{item.createDate}}</td>
              <td class="q-gutter-xs action">
                <a
                  class="text-primary"
                  href="javascript:;"
                  @click="onEdit(item)"
                >编辑</a>
                <a
                  class="text-primary"
                  href="javascript:;"
                  v-biz-delete:refresh="{data:{ id: item.id }, url:'/dictData/delete'}"
                >删除</a>
                <a
                  class="text-primary"
                  href="javascript:;"
                >新增下级数据</a>
              </td>
            </tr>
          </tbody>
        </q-markup-table>
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
      retData: [],
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
    this.onGetDictType(this.$route.params.dictType)
    this.onRefresh()
  },
  methods: {
    async onRefresh () {
      this.retData = await this.onRequestData({ id: '0' })
    },
    async onRequestData (node) {
      this.$set(node, 'loading', true)
      this.loading = true
      let _data
      await this.$fetchData({
        url: '/dictData/listData',
        method: 'GET',
        params: { pid: node.id, dictType: this.$route.params.dictType }
      }).then(response => {
        const { result, data } = response.data
        if (result && data) {
          _data = data
        }
      }).catch(error => {
        console.error(error)
      })
      setTimeout(_ => {
        this.$set(node, 'loading', false)
        this.loading = false
      }, 500)
      return _data
    },
    async expandRow (node, index) {
      if (!node.children) {
        const children = await this.onRequestData(node)
        if (children && children.length > 0) {
          if (!node.level) {
            this.$set(node, 'level', 1)
          }
          children.map((item, cindex) => {
            item.level = node.level + 1
            this.retData.splice((index + cindex + 1), 0, item)
          })
          this.$set(node, 'children', children)
          this.$set(node, 'show', true)
        }
      } else {
        this.$set(node, 'show', !node.show)
        const travel = (item) => {
          item.children && item.children.map(ele => {
            if (node.show) {
              if (item.show) {
                document.getElementById(ele.id).style = ''
              }
            } else {
              document.getElementById(ele.id).style = 'display: none'
            }
            if (ele.children && ele.children.length > 0) {
              travel(ele)
            }
          })
        }
        node.children && node.children.forEach(item => {
          document.getElementById(item.id).style = 'display:' + (node.show ? '' : 'none')
          travel(item)
        })
      }
    },
    async onExpand () {
      const temp = []
      const temp_ = []
      this.retData && this.retData.map(item => {
        if (!item.children && item.treeLeaf === '0') {
          // load data
          temp.push({ id: item.id, show: true })
        } else if (item.children && item.show === false) {
          // expand data
          temp_.push({ id: item.id, show: true })
        }
      })
      // 需要展开的数据
      if (temp_.length > 0) {
        this.pprf(temp_)
      } else {
        if (temp.length > 100) {
          console.warn('展开数据过多：' + temp.length)
        } else {
          this.pprf(temp)
        }
      }
    },
    pprf (nodes) {
      let p = Promise.resolve()
      for (let i = 0; i < nodes.length; i++) {
        p = p.then(_ => new Promise(resolve =>
          setTimeout(function () {
            var ele = document.getElementById('onc_' + nodes[i].id)
            if (ele && nodes[i].show) {
              ele.click()
            }
            resolve()
          }, 100)
        ))
      }
    },
    onCollapse () {
      const travel = (data) => {
        data && data.forEach(ele => {
          ele.show = false
          document.getElementById(ele.id).style = 'display: none'
          if (ele.children && ele.children.length > 0) {
            travel(ele.children)
          }
        })
      }
      this.retData && this.retData.forEach(item => {
        item.show = false
        if (item.children && item.children.length > 0) {
          travel(item.children)
        }
      })
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
