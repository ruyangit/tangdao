<script>
export default {
  name: 'TestItem',
  props: {
    item: Object
  },
  data: function () {
    return {
      isOpen: false
    }
  },
  computed: {
    isFolder: function () {
      return this.item.children && this.item.children.length
    }
  },
  methods: {
    toggle: function () {
      if (this.isFolder) {
        this.isOpen = !this.isOpen
      }
    }
  },
  render (h) {
    return h([h('tr', { id: 1 }, 'td'), h('tr', { id: 2 }, 'td')])
  }
}
</script>
