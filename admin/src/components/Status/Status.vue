<template>
  <q-badge
    :outline="outline"
    :color="status_data[value].color"
    :label="status_data[value].label"
  />
</template>
<script>
const status = {
  0: {
    label: '正常',
    color: 'positive'
  },
  2: {
    label: '无效',
    color: 'negative'
  }
}
export default {
  name: 'status',
  data () {
    return {
      status_data: status
    }
  },
  props: {
    value: {
      type: String,
      default: '0'
    },
    outline: {
      type: Boolean,
      default: true
    }
  }
}
</script>
