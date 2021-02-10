<template>
  <q-header
    elevated
    class="bg-primary text-white my-header"
    height-hint="98"
  >
    <q-toolbar>
      <q-avatar
        rounded
        color="blue"
        text-color="white"
        size="32px"
      >
        <img src="~assets/ms-logo-full.svg" />
      </q-avatar>

      <q-toolbar-title
        shrink
        class="text-bold logo-text-primary"
      >
        云通信 <span
          class="q-ml-xs"
          style="letter-spacing: 0.1em;font-size:12px;font-weight:500;"
        >CLOUD PRO5</span>
      </q-toolbar-title>
      <q-tabs
        dense
        narrow-indicator
        shrink
        align="left"
        v-if="$q.screen.gt.xs"
      >
        <q-route-tab
          :ripple="false"
          label="首页"
          to="/dashboard"
        />
        <q-route-tab
          :ripple="false"
          label="系统管理"
          to="/system"
        />
      </q-tabs>
      <q-space />
      <div>Quasar v{{ $q.version }}， {{ $q.lang.getLocale() }}</div>
    </q-toolbar>
  </q-header>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Header',
  computed: {
    ...mapGetters({
      leftDrawerOpen: {
        get () {
          return this.$store.state.basic.leftDrawerOpen
        },
        set (value) {
          this.$store.commit('basic/leftDrawerOpenMutAction', value)
        }
      }
    })
  }
}
</script>

<style lang="sass">
.my-header
.q-tab__content
  min-width: auto !important
</style>
