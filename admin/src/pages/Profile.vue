<template>
  <q-page class="my-page">
    <div class="row items-center justify-between">
      <div class="my-page-header">
        <q-breadcrumbs align="left">
          <q-breadcrumbs-el
            label="首页"
            to="/"
          />
          <q-breadcrumbs-el label="个人信息" />
        </q-breadcrumbs>
        <div class="row">
          <div>
            <div class="my-page-header-subtitle">我的工作台</div>
            <div class="q-mt-sm text-caption">你有 23条待处理的消息， 有 5项内容需要审核。</div>
          </div>
          <q-space />
          <div class="row wrap content-end">
            您好, system
          </div>
        </div>
      </div>
    </div>
    <div class="my-page-body">
      <q-card flat>
        <q-card-section horizontal>
          <q-card-section class="flex flex-center">
            <q-img
              class="rounded-borders"
              style="width:200px; height:150px;"
              src="https://cdn.quasar.dev/img/parallax2.jpg"
            />
          </q-card-section>
          <q-card-section class="full-width">
            <div class="row">
              <div class="">
                <div class="text-subtitle1 relative-position">
                  Nexa - Next generation SAAS
                  <q-icon
                    color="positive"
                    size="20px"
                    name="brightness_auto"
                    style="top:-2px"
                  />
                </div>
                <div class="q-mt-sm text-caption text-grey-8">
                  <q-chip
                    dense
                    color="deep-orange"
                    text-color="white"
                    icon="directions"
                  >
                    Get directions
                  </q-chip>
                </div>
              </div>
              <q-space />
              <div class="row wrap content-end">
                <q-btn
                  flat
                  color="primary"
                  label="创建任务1"
                />
                <q-btn
                  flat
                  color="primary"
                  label="创建任务2"
                />
              </div>
            </div>
            <div class="row q-mt-md">
              <div class="text-body2">
                I distinguish three main text objectives could be merely to inform people.<br />
                A second could be persuade people. You want people to bay objective.
              </div>
              <q-space />
              <div class="row wrap content-end q-col-gutter-lg">
                <!-- <div>
                  <p class="q-mb-sm">Due Date</p>
                  <q-btn
                    flat
                    style="background:#ffe2e5;color:#f64e60;"
                    label="10 June, 2021"
                  />
                </div>
                <div class="wd-200">
                  <p class="q-mb-sm">PROCESS</p>
                  <q-linear-progress
                    :value="0.67"
                    rounded
                  />
                  <p>67%</p>
                </div> -->
              </div>
            </div>
          </q-card-section>
        </q-card-section>
        <q-separator inset />
        <q-card-section class="row q-gutter-lg">
          <q-btn flat>
            <div class="row items-center no-wrap">
              <q-icon
                left
                name="paid"
                class="text-grey-6"
                size="38px"
              />
              <div class="text-center">
                Earnings<br>$249,500
              </div>
            </div>
          </q-btn>
          <q-btn flat>
            <div class="row items-center no-wrap">
              <q-icon
                left
                name="local_grocery_store"
                class="text-grey-6"
                size="38px"
              />
              <div class="text-center">
                Expenses<br>$164,700
              </div>
            </div>
          </q-btn>
          <q-btn flat>
            <div class="row items-center no-wrap">
              <q-icon
                left
                name="donut_large"
                class="text-grey-6"
                size="38px"
              />
              <div class="text-center">
                Net<br>$782,300
              </div>
            </div>
          </q-btn>
          <q-btn flat>
            <div class="row items-center no-wrap">
              <q-icon
                left
                name="all_inbox"
                class="text-grey-6"
                size="38px"
              />
              <div class="text-center">
                Inbox<br>320,032
              </div>
            </div>
          </q-btn>
        </q-card-section>
      </q-card>
      <div class="row q-col-gutter-md q-mt-md">
        <div class="col-9">
          <q-card flat>
            <q-card-section>我的工作任务</q-card-section>
            <q-card-section>Task</q-card-section>
          </q-card>
          <q-card
            flat
            class="q-mt-md"
          >
            <q-card-section>更多服务</q-card-section>
            <q-card-section>More Service</q-card-section>
          </q-card>
        </div>
        <div class="col-3">
          <q-card flat>
            <q-card-section>关键事项</q-card-section>
            <q-card-section>Content</q-card-section>
          </q-card>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
  name: 'Profile',
  meta: { title: '个人信息' },
  data () {
    return {
      loading: true,
      data: {}
    }
  },
  mounted () {
    this.onRequest()
  },
  methods: {
    onRequest () {

    }
  }
}
</script>
<style lang="sass" scoped>
</style>
