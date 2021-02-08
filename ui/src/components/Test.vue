<template>
  <q-card>
    <q-toolbar>
      <q-tabs dense align="left" :breakpoint="0">
        <q-tab label="全部" />
      </q-tabs>
      <q-space/>
      <div>搜索</div>
    </q-toolbar>
    <q-separator/>
    <q-card-section>
        <q-list>
        </q-list>
    </q-card-section>
</q-card>
</template>

<script>
export default {
  name: 'Test',
  props: {
    nodes: {
      type: Array,
      required: true
    },
    nodeKey: {
      type: String,
      required: true
    },
    labelKey: {
      type: String,
      default: 'label'
    },
    childrenKey: {
      type: String,
      default: 'children'
    },
    ticked: Array, // sync
    expanded: Array, // sync
    selected: {}, // sync

    defaultExpandAll: Boolean,
    filter: String,
    filterMethod: {
      type: Function,
      default (node, filter) {
        const filt = filter.toLowerCase()
        return node[this.labelKey] &&
          node[this.labelKey].toLowerCase().indexOf(filt) > -1
      }
    },
    duration: Number
  },
  computed: {
    hasSelection () {
      return this.selected !== undefined
    }
  },
  data () {
    return {
      lazy: {},
      innerTicked: this.ticked || [],
      innerExpanded: this.expanded || []
    }
  },
  watch: {
    ticked (val) {
      this.innerTicked = val
    },

    expanded (val) {
      this.innerExpanded = val
    }
  },
  methods: {
    getNodeByKey (key) {
      const reduce = [].reduce

      const find = (result, node) => {
        if (result || !node) {
          return result
        }
        if (Array.isArray(node) === true) {
          return reduce.call(Object(node), find, result)
        }
        if (node[this.nodeKey] === key) {
          return node
        }
        if (node[this.childrenKey]) {
          return find(null, node[this.childrenKey])
        }
      }

      return find(null, this.nodes)
    },

    getTickedNodes () {
      return this.innerTicked.map(key => this.getNodeByKey(key))
    }
  }
}
</script>

<style lang="sass" scoped>

</style>
