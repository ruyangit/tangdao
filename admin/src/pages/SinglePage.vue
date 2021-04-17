<template>
  <div>
    <img
      width="100%"
      src="img/vther.jpg"
    />
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
              v-for="(item,index) in 9"
              :key="index"
            >
              <q-card flat>
                <q-img src="https://placeimg.com/1024/768/nature" />

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
  </div>
</template>

<script>
export default {
  name: 'page1',
  components: {
  },
  data () {
    return {
      tab: 'mails'
    }
  }
}
</script>

<style lang="sass" scoped>
</style>
