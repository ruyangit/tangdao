<template>
  <q-page class="my-page">
    <div class="row items-center justify-between">
      <div class="my-page-header">
        <q-breadcrumbs align="left">
          <q-breadcrumbs-el
            label="首页"
            to="/"
          />
          <q-breadcrumbs-el
            label="系统设置"
            to="/system"
          />
          <q-breadcrumbs-el label="菜单管理" />
        </q-breadcrumbs>
      </div>
    </div>
    <div class="my-page-body">
      <q-splitter v-model="splitterModel">
        <template v-slot:before>
          <div class="q-pa-md">
            <div class="text-h4 q-mb-md">Before</div>
            <q-scroll-area class="fit">
              <div
                v-for="n in 40"
                :key="n"
                class="q-my-md"
              >{{ n }}. Lorem ipsum dolor sit</div>
            </q-scroll-area>
          </div>
        </template>

        <template v-slot:after>
          <div class="q-pa-md">
            <div class="text-h4 q-mb-md">After</div>
            <div
              v-for="n in 56"
              :key="n"
              class="q-my-md"
            >{{ n }}. Lorem ipsum dolor sit</div>
          </div>
        </template>

      </q-splitter>
    </div>
  </q-page>
</template>

<script>
export default {
  name: 'MenuIndex',
  data () {
    return {
      splitterModel: 50
    }
  }
}
</script>

<style lang="sass" scoped>
</style>
