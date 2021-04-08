<template>
  <q-dialog
    v-model="fixed"
    persistent
  >
    <Category
      v-model="selected"
      :treeData="list"
      bodyStyle="height:320px"
    >
      <q-card-section
        class="row items-center q-pb-md"
        slot="header"
      >
        <div class="text-h6">类型选择</div>
        <q-space />
        <q-btn
          round
          dense
          flat
          icon="search"
          size="12px"
        />
      </q-card-section>
      <q-card-actions
        slot="footer"
        class="row"
      >
        <!-- <div v-if="!selected && selected.length>0">已选择{{selected}}</div> -->
        {{selected}}
        <q-space />
        <q-btn
          flat
          label="取消"
          color="primary"
          class="wd-80"
          v-close-popup
        />
        <q-btn
          label="确认"
          color="primary"
          class="wd-80"
          @click="onSubmit"
        />
      </q-card-actions>
    </Category>
  </q-dialog>
</template>

<script>

import Category from './Category.vue'
export default {
  name: 'CategoryModal',
  components: {
    Category
  },
  props: {
    toggle: Boolean,
    value: Array
  },
  data () {
    return {
      selected: this.value,
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
  computed: {
    fixed: {
      get () {
        return this.toggle
      },
      set () {
        this.selected = this.value
        this.list = this.$options.data().list
        this.$emit('update:toggle', false)
      }
    }
  },
  watch: {
  },
  mounted () {
  },
  methods: {
    onSubmit () {
      this.$emit('input', this.selected)
      this.$emit('update:toggle', false)
    }
  }
}
</script>

<style lang="sass" scoped>
</style>
