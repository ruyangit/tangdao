<template>
  <q-card :style="`width: ${width || 560}px`">
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
        没有可用数据
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
// strategy: none,strict,leaf,some?
export default {
  name: 'Category',
  props: {
    treeData: {
      type: Array,
      default: () => { }
    },
    multiple: {
      type: Boolean,
      default: false
    },
    loading: {
      type: Boolean,
      default: false
    },
    width: {
      default: 560
    },
    bodyStyle: {
      default: 'max-height:360px'
    },
    value: Array
  },
  data () {
    return {
      tabs: [{ id: '0', label: '全部数据' }],
      tab: '0',
      tabIndex: 0,
      list: this.treeData,
      selected: this.value
      // ,selectedObject: []
    }
  },
  computed: {
    dataList () {
      if (this.selected.length === 0) {
        return this.list
      }
      if (this.list) {
        this.list.map(item => {
          if (this.selected.some(ele => ele === item.id)) {
            item.checkable = item.id
          }
        })
        return this.list
      }
      return []
    }
  },
  mounted () {
    if (!this.multiple) {
      if (this.selected && this.selected.length > 0) {
        this.selected.forEach(ele => {
          const data = this.getNode(ele, this.treeData)
          if (data && data.palls) {
            data.palls.forEach(ele => {
              this.openTab(ele)
            })
          }
        })
      }
    }
  },
  methods: {
    change (data) {
      if (this.multiple) {
        if (data.id === data.checkable) {
          if (this.selected.indexOf(data.id) < 0) {
            this.selected.push(data.id)
          }
        } else {
          this.selected.splice(this.selected.indexOf(data.id), 1)
        }
      } else {
        if (data.checkable) {
          this.selected = [data.id]
          this.list.map(item => {
            delete item.checkable
          })
        } else {
          this.selected = []
        }
      }
      this.$emit('input', this.selected)
    },
    focusTab (data, index) {
      this.tab = data.id
      this.tabIndex = index
      if (data.id === '0') {
        this.list = this.treeData
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
