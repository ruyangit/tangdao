<template>
  <q-layout view="hHh LpR lfr">
    <q-header />
    <q-page-container class="main-page-container">
      <div
        class="main-page-sidebar full-height"
        ref="pageLeft"
        v-if="sidebarVisibility"
        :style="`width: ${!$q.screen.gt.xs ? 0 : !sidebarLeftOpen ? sidebarMinimize : sidebar }px`"
      >
        <div class="sidebar-body">
          <q-scroll-area class="fit">
            <q-menu
              v-model="sidebarLeftOpen"
              :menus="sidebarMenus"
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
    <q-dialog
      v-model="resetLogin"
      persistent
    >
      <q-card style="width:400px">
        <q-card-section>
          <div class="text-h6">操作提示</div>
        </q-card-section>
        <q-separator />
        <q-card-section class="row items-center">
          <div class="q-ml-sm">{{reset.code}}</div>
          <div class="q-ml-sm">{{reset.message ||'未知异常，请重新登陆后操作'}}</div>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn
            flat
            label="关闭"
            color="primary"
            v-close-popup
          />
          <q-btn
            flat
            label="重新登录"
            color="primary"
            @click="$router.push({ path: '/user/login' })"
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
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
      sidebarMenus: []
    }
  },
  computed: {
    ...mapGetters({
      globalLoading: 'basic/globalLoading',
      menus: 'session/menus',
      reset: 'session/reset'
    }),
    resetLogin: {
      get () {
        return this.reset.login
      },
      set () {
        this.$store.commit('session/resetMutation', { login: false })
      }
    }
  },
  created () {
    this.$store.dispatch('session/menusAction')
    // this.sidebarMenusFn(this.$route)
    if (this.sidebarVisibility && this.$q.screen.gt.xs) {
      this.sidebarLeftOpen = false
    }
    if (this.sidebarVisibility && this.$q.screen.gt.sm) {
      this.sidebarLeftOpen = true
    }
  },
  mounted () {
    this.onLoadMenu(this.$route)
  },
  watch: {
    $route: 'onLoadMenu',
    'sidebarLeftOpen' (val) {
      if (this.sidebarVisibility) {
        this.$refs.pageLeft.setAttribute('style', 'width: ' + (val ? this.sidebar : this.sidebarMinimize) + 'px')
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
    onLoadMenu (route) {
      this.sidebarMenus = this.menus
      this.sidebarVisibility = route.meta && route.meta.sidebar === undefined
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
