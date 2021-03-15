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
                prefix="https:// "
                suffix=".com"
              >
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
                <q-popup-proxy
                  :cover="false"
                  anchor="top left"
                  :offset="[0,-42]"
                >
                  <q-card class="fit">
                    <q-card-section class="q-pa-none">
                      <q-tabs
                        align="left"
                        narrow-indicator
                      >
                        <q-tab
                          label="全部"
                          name="0"
                        />
                      </q-tabs>
                    </q-card-section>
                  </q-card>
                </q-popup-proxy>
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
              <q-tree
                class="col-12 col-sm-6"
                :nodes="tree.simple"
                node-key="label"
                tick-strategy="leaf-filtered"
                :selected.sync="tree.selected"
                :ticked.sync="tree.ticked"
                :expanded.sync="tree.expanded"
                :duration="0"
                no-connectors
              />
            </div>
            <div class="col-12 col-sm-6 col-lg-3 offset-lg-1">
              <label for="name">Ticked</label>
              <div
                v-for="tick in tree.ticked"
                :key="`ticked-${tick}`"
              >
                {{ tick }}
              </div>
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
      form: {
        name: '',
        ranges: { from: '2020/07/08', to: '2020/07/17' },
        date: '2021/02/04',
        poolId: '11101'
      },
      tree: {
        simple: [
          {
            label: '系统管理',
            children: [
              {
                label: '组织管理',
                children: [
                  { label: '用户管理' },
                  { label: '机构管理' },
                  { label: '公司管理' },
                  { label: '岗位管理' }
                ]
              },
              {
                label: '权限管理',
                children: [
                  { label: '角色管理' },
                  { label: '系统管理员' },
                  { label: '安全审计' }
                ]
              },
              {
                label: '系统设置',
                children: [
                  { label: '菜单管理' },
                  { label: '资源管理' },
                  { label: '参数管理' },
                  { label: '字典管理' },
                  { label: '行政区划' }
                ]
              },
              {
                label: '系统监控',
                children: [
                  { label: '系统日志' },
                  { label: '数据监控' },
                  { label: '缓存管理' },
                  { label: '服务器监控' },
                  { label: '作业管理' }
                ]
              }
            ]
          },
          { label: '消息管理' },
          { label: '流程管理' }
        ],
        ticked: ['资源管理', '系统日志'],
        expanded: ['系统管理', '系统设置', '系统监控']
      }
    }
  },
  methods: {
    onSubmit () {

    }
  }
}
</script>

<style lang="sass" scoped>
.basic-form
</style>
