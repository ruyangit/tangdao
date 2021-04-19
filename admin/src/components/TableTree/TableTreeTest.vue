<template>
  <div class="q-pa-md">
    <!-- {{retData}} -->
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
          <th class="text-left">Leaf</th>
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
              <div
                class="item-circle"
                v-if="item.leaf"
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
            </div>
            <span
              class="absolute"
              style="top:14px;"
            >{{item.name}}</span>
          </td>
          <td>{{item.level}}</td>
          <td>{{item.leaf}}</td>
        </tr>
      </tbody>
    </q-markup-table>
  </div>
</template>

<script>
export default {
  name: 'TreeTableTest',
  data () {
    return {
      loading: false,
      data: [
        { id: '1', pid: '0', name: 'test-1', avg: 122, leaf: false },
        { id: '1-1', pid: '1', name: 'test-1-1', avg: 312, leaf: false },
        { id: '1-1-1', pid: '1-1', name: 'test-1-1-1', avg: 122, leaf: true },
        { id: '1-1-2', pid: '1-1', name: 'test-1-1-2', avg: 121, leaf: false },
        { id: '1-1-2-1', pid: '1-1-2', name: 'test-1-1-2-1', avg: 121, leaf: true },
        { id: '1-1-2-2', pid: '1-1-2', name: 'test-1-1-2-2', avg: 121, leaf: true },
        { id: '1-2', pid: '1', name: 'test-1-2', avg: 123, leaf: false },
        { id: '1-2-1', pid: '1-2', name: 'test-1-2-1', avg: 123, leaf: true },
        { id: '1-2-2', pid: '1-2', name: 'test-1-2-2', avg: 123, leaf: true },
        { id: '2', pid: '0', name: 'test-2', avg: 122, leaf: false },
        { id: '2-1', pid: '2', name: 'test-2-1', avg: 112, leaf: true },
        { id: '2-2', pid: '2', name: 'test-2-2', avg: 122, leaf: true },
        { id: '2-3', pid: '2', name: 'test-2-3', avg: 152, leaf: true },
        { id: '2-4', pid: '2', name: 'test-2-4', avg: 142, leaf: true }
      ],
      retData: []
    }
  },
  mounted () {
    this.onRefresh()
  },
  methods: {
    onRefresh () {
      this.retData = this.$options.data().data.filter(item => { return item.pid === '0' })
    },
    onRequestData (node) {
      return this.$options.data().data.filter(item => { return item.pid === node.id })
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
      const temp = []
      this.retData && this.retData.map(item => {
        if (item.children && item.show === false) {
          temp.push({ id: item.id, show: true, leaf: item.leaf })
        } else if (!item.children && !item.leaf) {
          temp.push({ id: item.id, show: true, leaf: item.leaf })
        }
      })
      console.log(temp)
      temp && temp.map(item => {
        var ele = document.getElementById('onc_' + item.id)
        if (ele && item.show) {
          setTimeout(() => {
            ele.click()
          }, 200)
        }
      })
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

<style lang="sass" scoped>
.item-circle
  width: 8px
  height: 8px
  float: right
  border-radius: 100%
  border: 2px solid #bbdefb
  margin-right: 6px
</style>
