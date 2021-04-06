<template>
  <div>
    <img
      width="100%"
      src="img/vther.jpg"
    />
    <div
      class="q-pa-md"
      v-for="i in 3"
      :key="i"
    >
      <q-card
        flat
        bordered
      >
        <q-item>
          <q-item-section avatar>
            <q-skeleton type="QAvatar" />
          </q-item-section>

          <q-item-section>
            <q-item-label>
              <q-skeleton type="text" />
            </q-item-label>
            <q-item-label caption>
              <q-skeleton
                type="text"
                width="80%"
              />
            </q-item-label>
          </q-item-section>
        </q-item>

        <q-item>
          <q-item-section avatar />

          <q-item-section class="q-pl-sm">
            <q-skeleton
              height="150px"
              class="q-mb-sm"
            />

            <div class="row items-center justify-between no-wrap">
              <div class="row items-center">
                <q-icon
                  name="chat_bubble_outline"
                  color="grey-4"
                  class="q-mr-sm"
                  size="18px"
                />
                <q-skeleton
                  type="text"
                  width="30px"
                />
              </div>

              <div class="row items-center">
                <q-icon
                  name="repeat"
                  color="grey-4"
                  class="q-mr-sm"
                  size="18px"
                />
                <q-skeleton
                  type="text"
                  width="30px"
                />
              </div>

              <div class="row items-center">
                <q-icon
                  name="favorite_border"
                  color="grey-4"
                  class="q-mr-sm"
                  size="18px"
                />
                <q-skeleton
                  type="text"
                  width="30px"
                />
              </div>
            </div>
          </q-item-section>
        </q-item>
      </q-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'page1'
}
</script>

<style lang="sass" scoped>
</style>
