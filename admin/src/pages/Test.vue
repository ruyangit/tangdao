<template>
  <div class="q-pa-md">
    <!-- {{retData}} -->
    <q-markup-table class="my-table">
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
      data: [
        { id: '1', pid: '0', name: 'test-1', avg: 122 },
        { id: '1-1', pid: '1', name: 'test-1-1', avg: 312 },
        { id: '1-1-1', pid: '1-1', name: 'test-1-1-1', avg: 122 },
        { id: '1-1-2', pid: '1-1', name: 'test-1-1-2', avg: 121 },
        { id: '1-2', pid: '1', name: 'test-1-2', avg: 123 },
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
    onRequestData (node) {
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
        const temps = []
        const travel = (node) => {
          node.children.map(item => {
            temps.push(item)
            if (item.children && item.children.length > 0) {
              travel(item)
            }
          })
        }
        travel(node)
        temps.forEach(temp => {
          document.getElementById(temp.id).style = 'display:' + (node.show ? '' : 'none')
          // if (temp && !temp.show) {
          //   temp.children && temp.children.forEach(item => {
          //     document.getElementById(item.id).style = 'display:' + (temp.show ? '' : 'none')
          //   })
          // } else {
          //   console.log(temp)
          // }
        })
        // temps.map(item => {
        //   if (!item.show) {
        //     document.getElementById(item.id).style = 'display:' + (node.show ? '' : 'none')
        //   } else {
        //     console.log(item)
        //   }
        // })
      }
    }
  }
}
</script>

<style lang="sass" >
</style>
