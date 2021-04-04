<template>
  <q-header
    elevated
    class="bg-primary text-white my-header"
    height-hint="98"
  >
    <q-toolbar>
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
      <div
        class="q-gutter-sm"
        v-if="$q.screen.gt.sm"
      >
        <q-btn
          flat
          label="控制台"
          to="/overview"
        />
        <q-btn
          flat
          label="产品宣传页"
          to="/single"
        />
        <q-btn
          flat
          :label="`${$q.lang.nativeName}`"
          @click="watchLang"
        />
      </div>
      <q-space />

      <q-btn
        stretch
        flat
        icon="select_all"
        v-if="$q.screen.gt.xs"
      />
      <q-btn
        stretch
        flat
        :icon="`filter_tilt_shift`"
        v-if="$q.screen.gt.xs"
      >
        <q-menu>
          <q-list
            bordered
            padding
            class="rounded-borders"
            style="max-width: 350px"
          >
            <q-item-label header>Folders</q-item-label>

            <q-item
              clickable
              v-ripple
            >
              <q-item-section
                avatar
                top
              >
                <q-avatar
                  icon="folder"
                  color="primary"
                  text-color="white"
                />
              </q-item-section>

              <q-item-section>
                <q-item-label lines="1">Photos</q-item-label>
                <q-item-label caption>February 22nd, 2019</q-item-label>
              </q-item-section>

              <q-item-section side>
                <q-icon
                  name="info"
                  color="green"
                />
              </q-item-section>
            </q-item>

            <q-item
              clickable
              v-ripple
            >
              <q-item-section
                avatar
                top
              >
                <q-avatar
                  icon="folder"
                  color="orange"
                  text-color="white"
                />
              </q-item-section>

              <q-item-section>
                <q-item-label lines="1">Movies</q-item-label>
                <q-item-label caption>March 1st, 2019</q-item-label>
              </q-item-section>

              <q-item-section side>
                <q-icon name="info" />
              </q-item-section>
            </q-item>

            <q-item
              clickable
              v-ripple
            >
              <q-item-section
                avatar
                top
              >
                <q-avatar
                  icon="folder"
                  color="teal"
                  text-color="white"
                />
              </q-item-section>

              <q-item-section>
                <q-item-label lines="1">Photos</q-item-label>
                <q-item-label caption>January 15th, 2019</q-item-label>
              </q-item-section>

              <q-item-section side>
                <q-icon name="info" />
              </q-item-section>
            </q-item>

            <q-separator spaced />
            <q-item-label header>Files</q-item-label>

            <q-item
              clickable
              v-ripple
            >
              <q-item-section
                avatar
                top
              >
                <q-avatar
                  icon="assignment"
                  color="grey"
                  text-color="white"
                />
              </q-item-section>

              <q-item-section>
                <q-item-label lines="1">Expenses spreadsheet</q-item-label>
                <q-item-label caption>March 2nd, 2019</q-item-label>
              </q-item-section>

              <q-item-section side>
                <q-icon name="info" />
              </q-item-section>
            </q-item>

            <q-item
              clickable
              v-ripple
            >
              <q-item-section
                avatar
                top
              >
                <q-avatar
                  icon="place"
                  color="grey"
                  text-color="white"
                />
              </q-item-section>

              <q-item-section>
                <q-item-label lines="1">Places to visit</q-item-label>
                <q-item-label caption>February 22, 2019</q-item-label>
              </q-item-section>

              <q-item-section side>
                <q-icon
                  name="info"
                  color="amber"
                />
              </q-item-section>
            </q-item>

            <q-item
              clickable
              v-ripple
            >
              <q-item-section
                avatar
                top
              >
                <q-avatar
                  icon="library_music"
                  color="grey"
                  text-color="white"
                />
              </q-item-section>

              <q-item-section>
                <q-item-label lines="1">My favorite song</q-item-label>
                <q-item-label caption>Singing it all day</q-item-label>
              </q-item-section>

              <q-item-section side>
                <q-icon name="info" />
              </q-item-section>
            </q-item>
          </q-list>
        </q-menu>
      </q-btn>
      <q-btn
        stretch
        flat
        label="Account Settings"
        :icon="`img:https://cdn.quasar.dev/img/boy-avatar.png`"
      >
        <q-menu>
          <div class="row no-wrap q-pa-md">
            <div class="column">
              <div class="text-h6 q-mb-md">Settings</div>
              <q-toggle
                v-model="mobileData"
                label="Use Mobile Data"
              />
              <q-toggle
                v-model="bluetooth"
                label="Bluetooth"
              />
            </div>

            <q-separator
              vertical
              inset
              class="q-mx-lg"
            />

            <div class="column items-center">
              <q-avatar size="72px">
                <img src="https://cdn.quasar.dev/img/boy-avatar.png">
              </q-avatar>

              <div class="text-subtitle1 q-mt-md q-mb-xs">John Doe</div>

              <q-btn
                color="primary"
                label="Logout"
                push
                size="sm"
                @click="$router.push({ path: '/user/login' })"
              />
            </div>
          </div>
        </q-menu>
      </q-btn>
    </q-toolbar>
  </q-header>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Header',
  data () {
    return {
      mobileData: false,
      bluetooth: true
    }
  },
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
  },
  methods: {
    watchLang () {
      // console.log(this.$i18n.locale)
      if (this.$i18n.locale === 'en-us') {
        this.$i18n.locale = 'zh-hans'
        import(
          'quasar/lang/zh-hans'
        ).then(lang => {
          this.$q.lang.set(lang.default)
        })
      } else if (this.$i18n.locale === 'zh-hans') {
        this.$i18n.locale = 'en-us'
        import(
          'quasar/lang/en-us'
        ).then(lang => {
          this.$q.lang.set(lang.default)
        })
      }
    }
  }
}
</script>

<style lang="sass">
.q-tab__content
  min-width: auto !important
</style>
