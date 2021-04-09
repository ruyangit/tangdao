<template>
  <div>
    <img
      width="100%"
      src="img/vther.jpg"
    />
    <div class="q-pa-md full-width row  justify-center  ">
      <q-category-modal
        title="选择地址"
        v-model="value"
        :toggle.sync="toggle1"
        @finish="onFinish"
      />
      <q-btn
        label="选择地址"
        color="primary"
        @click="toggle1 = true"
      />
    </div>
    <div class="q-pa-md full-width row  justify-center  ">
      <q-input
        outlined
        dense
        no-error-icon
        v-model.trim="search"
        placeholder="搜索"
      >
        <template v-slot:append>
          <q-btn
            round
            flat
            dense
            icon="search"
            color="primary"
          />
        </template>
      </q-input>
    </div>
    <div class="q-pa-md full-width row  justify-center  ">
      <!-- <Category
        v-model="value"
        :options="list"
        :loading="loading"
        @finish="onFinish"
      >
      </Category> -->
    </div>
  </div>
</template>

<script>
// import Category from '../components/Category/Category.vue'
export default {
  name: 'page1',
  components: {
    // Category
  },
  data () {
    return {
      search: '',
      loading: true,
      toggle1: false,
      list1: [
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
      ],
      list: [],
      value: '341824',
      selected1: []
    }
  },
  watch: {
    // value () {
    //   console.log(this.value)
    // },
    // selected1 () {
    //   console.log(this.selected1)
    // }
  },
  mounted () {
    // this.onRequest()
  },
  methods: {
    async onRequest () {
      this.loading = true
      await this.$fetchData({
        url: '/api/tree',
        method: 'GET'
      }).then(response => {
        const { result, data } = response.data
        if (result && data) {
          this.list = data
        }
      })
      setTimeout(() => {
        this.loading = false
      }, 1000)
    },
    onFinish ({ selected, selectedOptions }) {
      console.log(selected)
      console.log(selectedOptions)
      // console.log(this.value)
    }
  }
}
</script>

<style lang="sass" scoped>
</style>
