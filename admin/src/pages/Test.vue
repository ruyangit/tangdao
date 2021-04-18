<template>
  <div class="q-pa-md">
    {{retData}}
    <q-card>
      <q-card-section class="q-gutter-xs">
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
          @click="onRefresh"
        />
      </q-card-section>
    </q-card>
    <q-markup-table class="my-table q-mt-md">
      <!-- <q-linear-progress indeterminate /> -->
      <thead>
        <tr>
          <th class="text-left wd-120">Dessert (100g serving)</th>
          <th class="text-left">Level</th>
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
              <q-icon
                :name="item.show?'arrow_drop_down':'arrow_right'"
                size="20px"
                color="primary"
                class="cursor-pointer"
                @click="expandRow(item, index)"
              />
            </div>
            <span
              class="absolute"
              style="top:14px;"
            >{{item.name}}</span>
          </td>
          <td>{{item.level}}</td>
        </tr>
      </tbody>
    </q-markup-table>
  </div>
</template>

<script>
export default {
  name: 'Test',
  components: {
  },
  data () {
    return {
      loading: false,
      data: [
        { id: '1', pid: '0', name: 'test-1', avg: 122 },
        { id: '1-1', pid: '1', name: 'test-1-1', avg: 312 },
        { id: '1-1-1', pid: '1-1', name: 'test-1-1-1', avg: 122 },
        { id: '1-1-2', pid: '1-1', name: 'test-1-1-2', avg: 121 },
        { id: '1-1-2-1', pid: '1-1-2', name: 'test-1-1-2-1', avg: 121 },
        { id: '1-1-2-2', pid: '1-1-2', name: 'test-1-1-2-2', avg: 121 },
        { id: '1-2', pid: '1', name: 'test-1-2', avg: 123 },
        { id: '1-2-1', pid: '1-2', name: 'test-1-2-1', avg: 123 },
        { id: '1-2-2', pid: '1-2', name: 'test-1-2-2', avg: 123 },
        { id: '2', pid: '0', name: 'test-2', avg: 122 },
        { id: '2-1', pid: '2', name: 'test-2-1', avg: 112 },
        { id: '2-2', pid: '2', name: 'test-2-2', avg: 122 },
        { id: '2-3', pid: '2', name: 'test-2-3', avg: 152 },
        { id: '2-4', pid: '2', name: 'test-2-4', avg: 142 }
      ],
      retData: []
    }
  },
  mounted () {
    this.retData = this.onRequestData({ id: '0' })
  },
  methods: {
    onRefresh () {
    },
    onRequestData (node) {
      this.loading = true
      setTimeout(() => {
        this.loading = false
      }, 1000)
      return this.data.filter(item => { return item.pid === node.id })
    },
    expandRow (node, index) {
      if (!node.children) {
        const children = this.onRequestData(node)
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
    onExpand () {
      this.retData && this.retData.forEach(item => {
        if (!item.children) {
          const children = this.onRequestData(item)
          if (children && children.length > 0) {
            this.$set(item, 'children', children)
          }
        }
      })
      console.log(this.retData)
      const _retData = []
      // const travel = (item) => {
      //   item.children && item.children.forEach(ele => {
      //     ele.level = item.level + 1
      //     _retData.push(ele)
      //     if (ele.children && ele.children.length > 0) {
      //       travel(ele)
      //     }
      //   })
      // }
      this.retData && this.retData.forEach(item => {
        item.level = 1
        _retData.push(item)
        if (item.children && item.children.length > 0) {
          // travel(item)
        }
      })
      console.log(_retData)
      this.retData = _retData
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
    }
  }
}
</script>

<style lang="sass" >
</style>
