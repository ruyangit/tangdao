<template>
  <q-header
    elevated
    class="bg-primary text-white my-header"
    height-hint="98"
  >
    <q-toolbar>
      <q-btn
        flat
        dense
        round
        icon="menu"
        aria-label="Menu"
      >
      </q-btn>
      <q-toolbar-title
        shrink
        class="text-bold logo-text-primary"
      >
        Quasar App <span
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
      <div>Quasar v{{ $q.version }}</div>
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
  // background: linear-gradient(145deg,$primary 11%,$primary 75%) !important
.q-logo
  img
    transform: rotate(0deg)
    transition: transform .8s ease-in-out
  &:hover img
    transform: rotate(-360deg)
.q-tab__content
  min-width: auto !important
</style>
