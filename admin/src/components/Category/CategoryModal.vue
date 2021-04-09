<template>
  <q-dialog
    v-model="fixed"
    persistent
  >
    <Category
      ref="category"
      v-model="selected"
      :options="list"
      :loading="loading"
      @finish="onFinish"
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
    value: [String, Number],
    toggle: Boolean
  },
  data () {
    return {
      loading: false,
      search: false,
      list: [],
      selected: this.value,
      selectedOptions: null
    }
  },
  computed: {
    fixed: {
      get () {
        return this.toggle
      },
      set () {
        this.selected = this.value
        this.selectedOptions = this.$options.data().selectedOptions
        this.$emit('update:toggle', false)
      }
    },
    treeNames () {
      if (this.selectedOptions) {
        const tempTreeNames = []
        if (this.selectedOptions.palls) {
          this.selectedOptions.palls.forEach(ele => {
            tempTreeNames.push(ele.label)
          })
        }
        tempTreeNames.push(this.selectedOptions.label)
        return tempTreeNames.join('>')
      }
      return null
    }
  },
  watch: {
    fixed () {
      if (this.fixed) {
        this.onRequest()
      } else {
        this.list = []
      }
    }
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
          this.list = data
        }
      })
      setTimeout(() => {
        this.loading = false
      }, 1000)
    },
    onFinish ({ selected, selectedOptions }) {
      this.selected = selected
      this.selectedOptions = selectedOptions
    },
    onSubmit () {
      this.$emit('input', this.selected)
      this.$emit('finish', {
        selected: this.selected,
        selectedOptions: this.selectedOptions
      })
      this.$emit('update:toggle', false)
    }
  }
}
</script>

<style lang="sass" scoped>
</style>
