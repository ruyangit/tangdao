<template>
  <div class="single-page">
    <img
      width="100%"
      src="img/vther.jpg"
    />
    <div class="header">
      <div class="container">
        <q-toolbar class="text-white q-mt-sm">
          <q-avatar
            rounded
            text-color="white"
            size="32px"
          >
            <img src="~assets/ms-logo-full.svg" />
          </q-avatar>

          <q-toolbar-title
            shrink
            class="text-bold logo-text-primary"
            v-if="$q.screen.gt.xs"
          >
            企业应用系统 <span
              class="q-ml-xs"
              style="letter-spacing: 0.1em;font-size:12px;font-weight:500;"
            >{{$q.version}}</span>
          </q-toolbar-title>
          <q-space />
          <q-btn
            flat
            icon="broken_image"
            label="控制台"
            to="/"
          />
        </q-toolbar>
      </div>
    </div>
    <div class="container">
      <q-tabs
        v-model="tab"
        align="left"
        class="text-primary q-mt-sm"
        style="background:#fff"
      >
        <q-tab
          name="mails"
          label="Followers 340"
        />
        <q-tab
          name="alarms"
          label="Following 1,563"
        />
        <q-tab
          name="alarms1"
          label="Request 19"
        />
      </q-tabs>
      <div class="row q-col-gutter-md">
        <div class="col-12 col-md-9 q-mt-md">
          <div class="row q-col-gutter-md">
            <div
              class="col-12 col-md-4 col-lg-3"
              v-for="(item,index) in products"
              :key="index"
            >
              <q-card flat>
                <q-img
                  :src="item"
                  style="width: 100%"
                />

                <q-card-section>
                  <q-btn
                    fab
                    color="primary"
                    icon="place"
                    class="absolute"
                    style="top: 0; right: 12px; transform: translateY(-50%);"
                  />

                  <div class="row no-wrap items-center">
                    <div class="col text-h6 ellipsis">
                      Cafe Basilico {{item}}
                    </div>
                    <div class="col-auto text-grey text-caption q-pt-md row no-wrap items-center">
                      <q-icon name="place" />
                      250 ft
                    </div>
                  </div>

                  <!-- <q-rating
                    v-model="stars"
                    :max="5"
                    size="32px"
                  /> -->
                </q-card-section>

                <q-card-section class="q-pt-none">
                  <div class="text-subtitle1">
                    $・Italian, Cafe
                  </div>
                  <div class="text-caption text-grey">
                    Small plates, salads & sandwiches in an intimate setting.
                  </div>
                </q-card-section>

                <q-separator />

                <q-card-actions>
                  <q-btn
                    flat
                    round
                    icon="event"
                  />
                  <q-btn
                    flat
                    color="primary"
                  >
                    Reserve
                  </q-btn>
                </q-card-actions>
              </q-card>
            </div>
          </div>
        </div>
        <div class="col-12 col-md-3 q-mt-md">
          <q-list style="background:#fff">
            <q-item-label header>List Header</q-item-label>

            <q-item>
              <q-item-section avatar>
                <q-icon
                  color="primary"
                  name="bluetooth"
                />
              </q-item-section>
              <q-item-section>List item</q-item-section>
              <q-item-section side>
                <q-item-label caption>meta</q-item-label>
              </q-item-section>
            </q-item>
          </q-list>
        </div>
      </div>
    </div>
    <div class="footer">
      <div class="footer1 row">
        <div class="container ">
          <div class="row q-col-gutter-md q-mt-md">
            <div
              class="col-12 col-sm-6 col-md-3"
              v-for="i in 3"
              :key="i"
            >
              <p class="text-subtitle2">Content - {{i}}</p>
              <p class="q-mb-sm text-caption">content</p>
              <p class="q-mb-sm text-caption">content</p>
              <p class="q-mb-sm text-caption">content</p>
              <p class="q-mb-sm text-caption">content</p>
            </div>
            <div class="col-12 col-sm-6 col-md-3">
              <q-img
                src="~assets/ms-logo-full.svg"
                style="width:100px;height:100px;"
              />
            </div>
          </div>
          <!-- <q-separator color="primary" /> -->
          <q-toolbar class="text-white q-pa-none q-mb-md q-mt-md">
            <q-avatar
              rounded
              text-color="white"
              size="32px"
            >
              <img src="~assets/ms-logo-full.svg" />
            </q-avatar>

            <q-toolbar-title
              shrink
              class="text-bold logo-text-primary"
              v-if="$q.screen.gt.xs"
            >
              企业应用系统
            </q-toolbar-title>
            <q-space />
            <q-btn
              outline
              label="联系我们"
              to="/"
            />
          </q-toolbar>
        </div>
      </div>
      <div class="footer2">
        <div class="container row">
          <div v-if="$q.screen.gt.xs">
            <span>© 公元 2021 辛丑年 Quasar v{{ $q.version }}.</span>
            <span class="q-ml-sm">Created by
              <a
                href="https://gitee.com/ruyangit/dashboard"
                class="text-primary"
              >ruyangit</a></span>
          </div>
          <q-space />
          <div class="flex q-gutter-xs text-primary">
            <span class="text-bold">中文</span>
            <q-separator vertical />
            <span>英文</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SinglePage',
  components: {
  },
  data () {
    return {
      tab: 'mails',
      products: [
        'https://ruyangit.gitee.io/2021/index.png',
        'https://cdn.quasar.dev/img/mountains.jpg',
        'https://cdn.quasar.dev/img/parallax1.jpg',
        'https://cdn.quasar.dev/img/parallax2.jpg',
        'https://cdn.quasar.dev/img/quasar.jpg'
      ]
    }
  }
}
</script>

<style lang="sass" scoped>
.single-page
  position: relative
  & .header
    position: absolute
    top: 0
    width: 100%
  & .footer
    width: 100%
    background: #5fc3fe
    & .footer1
    & .footer2
      font-size: 10px
      letter-spacing: .3px
      text-transform: uppercase
      padding: 15px 25px
      background-color: #96d5fb
</style>
