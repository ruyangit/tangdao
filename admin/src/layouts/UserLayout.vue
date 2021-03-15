<template>
  <q-layout class="layout-user">
    <q-page-container>
      <router-view />
    </q-page-container>
    <q-footer class="flex items-center justify-between footer">
      <div v-if="$q.screen.gt.xs">
        <span>© 公元 2021 辛丑年 Quasar v{{ $q.version }}.</span>
        <span class="q-ml-sm"
          >Created by
          <a href="https://gitee.com/ruyangit/dashboard">ruyangit</a></span
        >
      </div>
      <div class="flex q-gutter-xs q-mt-none text-primary">
        <span class="text-bold">中文</span>
        <q-separator vertical />
        <span>英文</span>
      </div>
    </q-footer>
  </q-layout>
</template>

<script>
export default {
  name: 'UserLayout'
}
</script>

<style lang="sass" scoped>
.layout-user
  background-image: url(~assets/user-layout-bg.svg)
  background-repeat: no-repeat
  background-position: center 0px
  background-size: 100%
  & > footer
    font-size: 10px
    letter-spacing: .3px
    text-transform: uppercase
    padding: 15px 25px
    color: #637388
    background-color: #fafbfc
</style>
