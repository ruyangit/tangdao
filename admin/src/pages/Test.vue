<template>
  <div class="q-pa-md">
    {{retData}}
    <q-markup-table>
      <thead>
        <tr>
          <th class="text-left wd-120">Dessert (100g serving)</th>
          <!-- <th class="text-right">Calories</th>
          <th class="text-right">Fat (g)</th>
          <th class="text-right">Carbs (g)</th>
          <th class="text-right">Protein (g)</th> -->
        </tr>
      </thead>
      <tbody>
        <test-item
          v-for="item in retData"
          :item="item"
          :key="item.id"
        />
      </tbody>
    </q-markup-table>
  </div>
</template>

<script>
import TestItem from './TestItem.vue'
export default {
  name: 'Test',
  components: {
    TestItem
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
    expandRow (node) {
      const children = this.onRequestData(node)
      this.$set(node, 'children', children)
      this.$nextTick(() => {
        // 展开
        console.log(this.retData)
      })
    }
  }
}
</script>

<style lang="sass" scoped>
</style>
