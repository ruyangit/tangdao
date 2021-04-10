<template>
  <q-card style="width: 560px">
    <slot name="header"></slot>
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
      :style="bodyStyle"
      class="scroll"
    >
      <div
        class="row justify-center"
        v-if="dataList.length===0"
      >
        <span v-if="loading">正在加载...</span>
        <span v-else>没有可用数据</span>
      </div>
      <div
        class="row q-col-gutter-xs"
        v-else
      >
        <div
          class="col-3 q-pa-xs my-item"
          v-for="item in dataList"
          :key="item.id"
        >
          <div @click="openTab(item)">
            <q-radio
              dense
              v-model="selected"
              :val="item.id"
              :label="!item.children?item.label:null"
              :disable="(item.status && item.status === '2') ? true : false"
              @input="change(item)"
            />
            <span v-if="item.children">{{item.label}}（{{item.children.length}}）</span>
          </div>
        </div>
      </div>
      <q-inner-loading :showing="loading">
        <q-spinner-hourglass
          size="sm"
          color="primary"
        />
      </q-inner-loading>
    </q-card-section>
    <slot name="footer"></slot>
  </q-card>
</template>

<script>
export default {
  name: 'Category',
  props: {
    options: Array,
    value: [String, Number],
    loading: {
      type: Boolean,
      default: false
    },
    bodyStyle: {
      type: String,
      default: 'max-height:350px'
    }
  },
  data () {
    return {
      tabs: [{ id: '0', label: '全部数据' }],
      tab: '0',
      tabIndex: 0,
      list: this.options,
      selected: this.value
    }
  },
  computed: {
    dataList () {
      if (this.list.length === 0) {
        return this.options
      }
      return this.list
    }
  },
  watch: {
    options () {
      this.load()
    }
  },
  methods: {
    load () {
      const data = this.getNode(this.value, this.options)
      if (data && data.palls) {
        data.palls.forEach(ele => {
          this.openTab(ele)
        })
      }
      this.change({ id: this.value })
    },
    change (data) {
      const eventParams = {
        selected: this.selected,
        selectedOptions: this.getNode(data.id, this.options)
      }
      this.$emit('finish', eventParams)
    },
    focusTab (data, index) {
      this.tab = data.id
      this.tabIndex = index
      if (data.id === '0') {
        this.list = this.options
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
        this.change(data)
      }
    },
    getNode (id, data) {
      if (!id || !data) {
        return null
      }
      var temp = null
      var tempTree = []
      const travel = (id, data) => {
        return data.some(ele => {
          if (ele.id === id) {
            temp = ele
            return true
          }
          if (ele.children && ele.children.length) {
            const ret = travel(id, ele.children)
            if (temp && tempTree.indexOf(temp) < 0) {
              tempTree.unshift(ele)
            }
            return ret
          }
        })
      }
      travel(id, data)
      if (temp) {
        temp.palls = tempTree
      }
      return temp
    }
  }
}
</script>

<style lang="sass" scoped>
.my-item:hover
  background-color: #eee
  cursor: pointer
</style>
