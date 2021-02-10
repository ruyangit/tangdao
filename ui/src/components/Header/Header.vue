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
      >
        Short <span
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
      <q-btn
        stretch
        flat
        :label="`Quasar v${ $q.version }`"
      />
      <q-btn-dropdown
        stretch
        flat
        dropdown-icon="filter_tilt_shift"
      >
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
      </q-btn-dropdown>
      <q-btn
        stretch
        flat
        :label="`${ $q.lang.getLocale() }`"
      />
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
