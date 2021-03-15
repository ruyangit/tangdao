<template>
  <q-layout view="hHh LpR lfr">
    <q-header />
    <q-page-container class="main-page-container">
      <div
        class="main-page-sidebar full-height"
        ref="pageSidebar"
        v-if="sidebarVisibility"
        :style="`width: ${!$q.screen.gt.xs ? 0 : !sidebarLeftOpen ? sidebarMinimize : sidebar }px`"
      >
        <div class="sidebar-body">
          <q-scroll-area class="fit">
            <q-menu
              v-model="sidebarLeftOpen"
              :menus="menus"
            />
          </q-scroll-area>
        </div>
        <div
          class="sidebar-footer row items-center"
          v-if="$q.screen.gt.xs"
        >
          <q-btn
            flat
            dense
            round
            @click="sidebarLeftOpen = !sidebarLeftOpen"
            :icon="`${sidebarLeftOpen?'format_indent_decrease':'format_indent_increase'}`"
            aria-label="Menu"
            color="primary"
            size="sm"
          />
        </div>
      </div>
      <div
        class="main-page-body"
        ref="pageBody"
        :style="`left: ${!$q.screen.gt.xs || !sidebarVisibility ? 0 : !sidebarLeftOpen ? sidebarMinimize : sidebar }px`"
      >
        <div class="full-height scroll">
          <router-view />
        </div>
      </div>
    </q-page-container>
    <q-inner-loading :showing="globalLoading">
      <q-spinner-oval
        size="28px"
        color="primary"
      />
      <span class="text-primary text-overline">正在加载请稍后 ...</span>
    </q-inner-loading>
  </q-layout>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'MainLayout',
  components: {
    QHeader: () => import('components/Header/Header'),
    QMenu: () => import('components/Menu/Menu')
  },
  data () {
    return {
      sidebar: 240,
      sidebarMinimize: 55,
      sidebarVisibility: false,
      sidebarLeftOpen: true,
      menus: []
    }
  },
  computed: {
    ...mapGetters({
      globalLoading: 'basic/globalLoading',
      sidebarMenus: 'session/sidebarMenus'
    })
  },
  created () {
    this.onRequest()
    this.sidebarMenusFn(this.$route)
    if (this.sidebarVisibility && this.$q.screen.gt.xs) {
      this.sidebarLeftOpen = false
    }
    if (this.sidebarVisibility && this.$q.screen.gt.sm) {
      this.sidebarLeftOpen = true
    }
  },
  watch: {
    $route: 'sidebarMenusFn',
    'sidebarLeftOpen' (val) {
      if (this.sidebarVisibility) {
        this.$refs.pageSidebar.setAttribute('style', 'width: ' + (val ? this.sidebar : this.sidebarMinimize) + 'px')
        this.$refs.pageBody.setAttribute('style', 'left: ' + (val ? this.sidebar : this.sidebarMinimize) + 'px')
      }
    },
    '$q.screen.gt.sm' (val) {
      if (this.sidebarVisibility) {
        this.sidebarLeftOpen = val
      }
    },
    '$q.screen.gt.xs' () {
      if (this.sidebarVisibility) {
        this.sidebarLeftOpen = false
      }
    }
  },

  methods: {
    sidebarMenusFn (route) {
      if (route.meta && route.meta.sidebar === undefined) {
        this.sidebarVisibility = true
        const { path } = route.matched[1]
        this.menus = this.sidebarMenus.filter(item => item.children && item.path === path)
      } else {
        this.sidebarVisibility = false
        this.menus = []
      }
    },
    async onRequest () {
      await this.$store.dispatch('session/menusAction')
    }

  }
}
</script>
<style lang="sass" scoped>
.main-page
  &-container
    position: absolute
    top: 50px
    left: 0
    right: 0
    bottom: 0
    overflow: hidden
    padding-top: 0px !important
  &-sidebar
    position: absolute
    border-right: 1px solid #eaebec
    background-color: #fff
    & .sidebar-body
      position: absolute
      top: 0
      left: 0
      right: 0
      bottom: 45px
    & .sidebar-footer
      position: absolute
      left: 0
      right: 0
      bottom: 0
      height: 45px
      padding-left: 13.5px
  &-body
    position: absolute
    top: 0
    right: 0
    bottom: 0
    overflow-y: auto
    overflow-x: hidden
</style>
