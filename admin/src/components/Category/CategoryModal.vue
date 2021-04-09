<template>
  <q-dialog
    v-model="fixed"
    persistent
  >
    <Category
      ref="category"
      v-model="selected"
      :options="lists"
      :loading="loading"
      bodyStyle="height:320px"
    >
      <q-card-section
        class="row items-center q-pb-md"
        slot="header"
      >
        <div
          class="text-h6"
          v-text="title"
        ></div>
        <q-space />
        <q-btn
          round
          dense
          flat
          icon="search"
          size="12px"
        />
      </q-card-section>
      <q-card-actions
        slot="footer"
        class="row"
      >
        <div v-if="treeNames">{{treeNames}}</div>
        <q-space />
        <q-btn
          flat
          label="取消"
          color="primary"
          class="wd-80"
          v-close-popup
        />
        <q-btn
          label="确认"
          color="primary"
          class="wd-80"
          @click="onSubmit"
        />
      </q-card-actions>
    </Category>
  </q-dialog>
</template>

<script>

import Category from './Category.vue'
export default {
  name: 'CategoryModal',
  components: {
    Category
  },
  props: {
    title: {
      type: String,
      default: ''
    },
    toggle: Boolean,
    value: Array
  },
  data () {
    return {
      loading: false,
      search: false,
      selected: this.value,
      selectedObject: [],
      lists: []
    }
  },
  computed: {
    fixed: {
      get () {
        return this.toggle
      },
      set () {
        this.selected = this.value
        // this.lists = this.$options.data().lists
        this.$emit('update:toggle', false)
      }
    },
    treeNames () {
      if (this.selectedObject[0]) {
        const tempTreeNames = []
        if (this.selectedObject[0].palls) {
          this.selectedObject[0].palls.forEach(ele => {
            tempTreeNames.push(ele.label)
          })
        }
        tempTreeNames.push(this.selectedObject[0].label)
        return tempTreeNames.join('>')
      }
      return null
    }
  },
  created () {
    this.onRequest()
  },
  watch: {
    selected () {
      if (this.selected && this.selected[0]) {
        const node = this.$refs.category.getNode(this.selected[0], this.lists)
        this.selectedObject = [node]
      } else {
        this.selectedObject = []
      }
    }
    // ,
    // fixed () {
    //   if (this.fixed && this.lists.length === 0) {
    //     this.getData()
    //   }
    // }
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
          this.lists = data
        }
      })
      setTimeout(() => {
        this.loading = false
      }, 1000)
    },
    onSubmit () {
      const selectedOptions = this.$refs.category.getNode(this.selected[0], this.lists)
      this.$emit('finish', { selectedOptions })
      this.$emit('input', this.selected)
      this.$emit('update:toggle', false)
    }
  }
}
</script>

<style lang="sass" scoped>
</style>
