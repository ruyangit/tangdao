<template>
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
</template>

<script>
export default {
  name: 'CategoryModal',
  props: {
    treeData: {
      type: Array,
      default: () => { }
    }
  },
  data () {
    return {
      tabs: [{ id: '0', label: '全部数据' }],
      tab: '0',
      tabIndex: 0,
      tabValue: '0',
      list: this.treeData
    }
  },
  mounted () {
    console.log(this.treeData)
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
        this.change(data.id)
      }
    }
  }
}
</script>

<style lang="sass" scoped>
.my-item:hover
  background-color: #eee
  cursor: pointer
</style>
