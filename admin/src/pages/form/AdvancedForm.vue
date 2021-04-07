<template>
  <q-page class="my-page basic-form">
    <div class="row items-center justify-between">
      <div class="my-page-header">
        <q-breadcrumbs align="left">
          <q-breadcrumbs-el
            label="首页"
            to=""
          />
          <q-breadcrumbs-el
            label="表单页面"
            to=""
          />
          <q-breadcrumbs-el label="高级表单" />
        </q-breadcrumbs>
        <div class="my-page-header-subtitle">高级表单</div>
        <div class="q-mt-sm">高级表单常见于一次性输入和提交大批量数据的场景。</div>
      </div>
    </div>
    <div class="my-page-body">
      <q-form
        class="my-form"
        @submit="onSubmit"
      >
        <q-card
          flat
          class="fit"
        >
          <q-card-section>仓库管理</q-card-section>
          <q-separator />
          <q-card-section class="row q-col-gutter-md">
            <div class="col-12 col-sm-6 col-lg-3">
              <label for="name">仓库名称</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.name"
                placeholder="请输入仓库名称"
                :rules="[ val => val && val.length > 0 || '请设置仓库名称']"
                class="q-mt-sm"
              >
              </q-input>
            </div>
            <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
              <label for="name">仓库域名</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.name"
                placeholder="请输入仓库域名"
                :rules="[ val => val && val.length > 0 || '请设置仓库域名']"
                class="q-mt-sm"
              >
                <template v-slot:append>
                  <q-icon
                    name="event"
                    color="orange"
                  />
                </template>
              </q-input>
            </div>
            <div class="col-12 col-sm-12 col-lg-4 offset-lg-1">
              <label for="name">仓库管理员</label>
              <q-select
                outlined
                dense
                options-dense
                v-model="form.name"
                :options="['AAA','BBB','CCC']"
                emit-value
                map-options
                class="q-mt-sm"
              />
            </div>
          </q-card-section>
          <q-card-section class="row q-col-gutter-md ">
            <div class="col-12 col-sm-6 col-lg-3">
              <label for="name">审批人</label>
              <q-select
                outlined
                dense
                options-dense
                v-model="form.name"
                :options="['EEE','FFF']"
                emit-value
                map-options
                class="q-mt-sm"
              />
            </div>
            <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
              <label for="name">生效日期</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.ranges.from"
                placeholder="请选择生效日期"
                :rules="[ val => val && val.length > 0 || '请选择生效日期']"
                class="q-mt-sm"
              >
                <q-popup-proxy
                  :cover="false"
                  anchor="top left"
                  :offset="[0,-42]"
                >
                  <q-date
                    v-model="form.ranges"
                    landscape
                    range
                  ></q-date>
                </q-popup-proxy>
              </q-input>
            </div>
            <div class="col-12 col-sm-12 col-lg-4 offset-lg-1">
              <label for="name">仓库类型</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.poolId"
                placeholder="请选择仓库类型"
                :rules="[ val => val && val.length > 0 || '请选择仓库类型']"
                class="q-mt-sm"
              >
                <template v-slot:append>
                  <q-btn
                    flat
                    dense
                    icon="rotate_90_degrees_ccw"
                    color="primary"
                  />
                </template>

                <q-dialog
                  v-model="demo"
                  persistent
                >
                  <q-card style="width: 560px">
                    <q-card-section class="row items-center q-pb-md">
                      <div class="text-h6">类型选择 - {{tabValue}}</div>
                      <q-space />
                      <q-btn
                        round
                        dense
                        flat
                        icon="search"
                        size="12px"
                      />
                    </q-card-section>
                    <q-card-section class="q-pa-none">
                      <q-tabs
                        dense
                        v-model="tab"
                        active-color="primary"
                        indicator-color="primary"
                        align="left"
                        class="text-grey"
                        :breakpoint="0"
                        narrow-indicator
                      >
                        <q-tab
                          v-for="(item, index) in tabs"
                          :key="item.id"
                          :name="item.id"
                          :label="item.label"
                          @click="focusTab(item, index)"
                        />
                      </q-tabs>
                    </q-card-section>
                    <q-separator />
                    <q-card-section
                      style="height: 380px;"
                      class="scroll"
                    >
                      <div class="row q-col-gutter-xs">
                        <div
                          class="col-3 q-pa-xs my-item"
                          v-for="item in list"
                          :key="item.id"
                        >
                          <div
                            class=""
                            @click="openTab(item)"
                          >
                            <q-checkbox
                              dense
                              v-model="item.checkable"
                              :trueValue="item.id"
                              :falseValue="null"
                              :label="!item.children?item.label:null"
                              @input="change"
                            />
                            <span v-if="item.children">{{item.label}}（{{item.children.length}}）</span>
                          </div>
                        </div>
                      </div>
                    </q-card-section>
                  </q-card>
                </q-dialog>

                <!-- <q-popup-proxy
                  :cover="false"
                  anchor="top left"
                  :offset="[0,-42]"
                >
                  <q-tabs
                    align="left"
                    dense
                    class="text-grey"
                    active-color="primary"
                    indicator-color="primary"
                    narrow-indicator
                  >
                    <q-tab
                      label="全部"
                      name="0"
                    />
                    <q-tab
                      label="一级别（12）"
                      name="1"
                    />
                    <q-tab
                      label="一级别（21）"
                      name="2"
                    />
                  </q-tabs>
                </q-popup-proxy> -->
              </q-input>
            </div>
          </q-card-section>
        </q-card>
        <!-- <q-card flat class="fit q-mt-md">
          <q-card-section>任务管理</q-card-section>
          <q-separator />
          <q-card-section class="row q-col-gutter-md">
              <div class="col-12 col-sm-6 col-lg-3">
                 <label for="name">任务名</label>
                  <q-input
                    outlined
                    dense
                    no-error-icon
                    v-model.trim="form.name"
                    placeholder="请输入仓库名称"
                    :rules="[ val => val && val.length > 0 || '请设置仓库名称']"
                    class="q-mt-sm"
                  >
                  </q-input>
              </div>
              <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
                <label for="name">任务描述</label>
                  <q-input
                    outlined
                    dense
                    no-error-icon
                    v-model.trim="form.name"
                    placeholder="请输入仓库域名"
                    :rules="[ val => val && val.length > 0 || '请设置仓库域名']"
                    class="q-mt-sm"
                  >
                  </q-input>
              </div>
              <div class="col-12 col-sm-12 col-lg-4 offset-lg-1">
                <label for="name">执行人</label>
                  <q-select
                        outlined
                        dense
                        options-dense
                        v-model="form.name"
                        :options="['AAA','BBB','CCC']"
                        emit-value
                        map-options
                        class="q-mt-sm"
                      />
              </div>
          </q-card-section>
          <q-card-section class="row q-col-gutter-md ">
              <div class="col-12 col-sm-6 col-lg-3">
                 <label for="name">责任人</label>
                  <q-select
                        outlined
                        dense
                        options-dense
                        v-model="form.name"
                        :options="['EEE','FFF']"
                        emit-value
                        map-options
                        class="q-mt-sm"
                      />
              </div>
              <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
                <label for="name">生效日期</label>
                  <q-input
                    outlined
                    dense
                    no-error-icon
                    v-model.trim="form.date"
                    placeholder="请选择生效日期"
                    :rules="[ val => val && val.length > 0 || '请选择生效日期']"
                    class="q-mt-sm"
                  >
                    <q-popup-proxy :cover="false"  anchor="top left" :offset="[0,-42]">
                      <q-date v-model="form.date" landscape></q-date>
                    </q-popup-proxy>
                  </q-input>
              </div>
              <div class="col-12 col-sm-12 col-lg-4 offset-lg-1">
                <label for="name">任务类型</label>
                  <q-select
                        outlined
                        dense
                        options-dense
                        v-model="form.name"
                        :options="['OOO','DDDDD','SSSSS','B','B']"
                        emit-value
                        map-options
                        class="q-mt-sm"
                      />
              </div>
          </q-card-section>
        </q-card> -->
        <q-card
          flat
          class="fit q-mt-md"
        >
          <q-card-section>组件测试</q-card-section>
          <q-separator />
          <q-card-section class="row q-col-gutter-md">
            <div class="col-12 col-sm-6 col-lg-3">
              <label for="name">Tree</label>
              <!-- <q-tree
                class="col-12 col-sm-6"
                :nodes="tree.simple"
                node-key="label"
                tick-strategy="leaf-filtered"
                :selected.sync="tree.selected"
                :ticked.sync="tree.ticked"
                :expanded.sync="tree.expanded"
                :duration="0"
                no-connectors
              /> -->
            </div>
            <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
              <label for="name">Ticked</label>
              <!-- <div
                v-for="tick in tree.ticked"
                :key="`ticked-${tick}`"
              >
                {{ tick }}
              </div> -->
            </div>
            <div class="col-12 col-sm-12 col-lg-4 offset-lg-1">
              <label for="name">类型选择</label>
              <div>
              </div>
            </div>
          </q-card-section>
          <q-card-section class="row q-col-gutter-md ">
            <div class="col-12 col-sm-6 col-lg-3">
              <label for="name">责任人</label>
              <q-select
                outlined
                dense
                options-dense
                v-model="form.name"
                :options="['EEE','FFF']"
                emit-value
                map-options
                class="q-mt-sm"
              />
            </div>
            <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
              <label for="name">生效日期</label>
              <q-input
                outlined
                dense
                no-error-icon
                v-model.trim="form.date"
                placeholder="请选择生效日期"
                :rules="[ val => val && val.length > 0 || '请选择生效日期']"
                class="q-mt-sm"
              >
                <q-popup-proxy
                  :cover="false"
                  anchor="top left"
                  :offset="[0,-42]"
                >
                  <q-date
                    v-model="form.date"
                    landscape
                  ></q-date>
                </q-popup-proxy>
              </q-input>
            </div>
            <div class="col-12 col-sm-12 col-lg-4 offset-lg-1">
              <label for="name">任务类型</label>
              <q-select
                outlined
                dense
                options-dense
                v-model="form.name"
                :options="['OOO','DDDDD','SSSSS','B','B']"
                emit-value
                map-options
                class="q-mt-sm"
              />
            </div>
          </q-card-section>
        </q-card>
      </q-form>
    </div>
  </q-page>
</template>

<script>
export default {
  name: 'AdvancedForm',
  components: {
  },
  meta: {
    title: '高级表单'
  },
  data () {
    return {
      demo: false,
      // demo_i: '0',
      // demo_data: [],
      tabs: [{ id: '0', label: '全部数据' }],
      tab: '0',
      tabIndex: 0,
      tabValue: '0',
      form: {
        name: '',
        ranges: { from: '2020/07/08', to: '2020/07/17' },
        date: '2021/02/04',
        poolId: '11101'
      },
      list: [
        {
          id: '1',
          label: '系统管理',
          children: [
            {
              id: '1-1',
              label: '组织管理',
              children: [
                { id: '1-1-1', label: '用户管理' },
                { id: '1-1-2', label: '机构管理' },
                { id: '1-1-3', label: '公司管理' },
                { id: '1-1-4', label: '岗位管理' }
              ]
            },
            {
              id: '1-2',
              label: '权限管理',
              children: [
                { id: '1-2-1', label: '角色管理' },
                { id: '1-2-2', label: '系统管理员' },
                { id: '1-2-3', label: '安全审计' }
              ]
            },
            {
              id: '1-3',
              label: '系统设置',
              children: [
                { id: '1-3-1', label: '菜单管理' },
                { id: '1-3-2', label: '资源管理' },
                { id: '1-3-3', label: '参数管理' },
                { id: '1-3-4', label: '字典管理' },
                { id: '1-3-5', label: '行政区划' }
              ]
            },
            {
              id: '1-4',
              label: '系统监控',
              children: [
                { id: '1-4-1', label: '系统日志' },
                { id: '1-4-2', label: '数据监控' },
                { id: '1-4-3', label: '缓存管理' },
                { id: '1-4-4', label: '服务器监控' },
                { id: '1-4-5', label: '作业管理' }
              ]
            }
          ]
        },
        { id: '2', label: '消息管理' },
        { id: '3', label: '流程管理' },
        { id: '4', label: '企业' },
        { id: '5', label: '办公' },
        { id: '6', label: '客户' },
        { id: '7', label: '设置' }
      ]
    }
  },
  watch: {
    tabValue () {
    }
  },
  methods: {
    change (value, event) {
      if (event) {
        event.stopPropagation()
        event.preventDefault()
      }
      this.tabValue = value
    },
    focusTab (data, index) {
      this.tab = data.id
      this.tabIndex = index
      if (data.id === '0') {
        this.list = this.$options.data().list
      } else {
        this.list = data.children
      }
    },
    openTab (data) {
      if (data.children && data.children.length) {
        this.tabIndex = this.tabIndex + 1
        this.tabs.splice(this.tabIndex)
        this.tabs.push(data)
        this.tab = data.id
        this.list = data.children
      } else {
        this.change(data.id)
      }
    },
    onSubmit () {

    }
  }
}
</script>

<style lang="sass" scoped>
.my-item:hover
  background-color: #eee
  cursor: pointer
</style>
