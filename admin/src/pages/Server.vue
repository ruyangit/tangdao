<template>
  <q-page class="my-page">
    <div class="row items-center justify-between">
      <div class="my-page-header">
        <q-breadcrumbs align="left">
          <q-breadcrumbs-el
            label="首页"
            to="/"
          />
          <q-breadcrumbs-el label="服务器监控" />
        </q-breadcrumbs>
        <div class="row q-col-gutter-md">
          <div>
            <div class="my-page-header-subtitle relative-position">
              系统运行状况
              <q-btn
                dense
                flat
                color="positive"
                :loading="loading"
                style="top:-2px"
              >
                <q-icon name="check_circle" />
              </q-btn>
            </div>
          </div>
          <q-space />
          <div class="row wrap content-end text-subtitle2">
            {{data.oshiOsName}}({{data.oshiOsArch}})
          </div>
        </div>
      </div>
    </div>
    <div class="my-page-body">
      <div class="row container">
        <div class="col-12 col-md-6 col-lg-4 q-pa-sm">
          <q-card>
            <q-card-section>
              <div class="text-h6">CPU</div>
              <div class="text-subtitle2">{{data.oshiProcessorIdentifierName}}</div>
            </q-card-section>
            <q-card-section class="text-h4">
              {{data.oshiProcessorLogicalProcessorCount + ' Cores'}}
            </q-card-section>
          </q-card>
          <q-card
            class="q-mt-md"
            v-if="data.hostInfo"
          >
            <q-card-section>
              <div class="text-h6">Host</div>
              <div class="text-subtitle2">{{data.hostInfo.name}}</div>
            </q-card-section>
            <q-card-section>
              {{data.hostInfo.address}}
            </q-card-section>
          </q-card>
        </div>
        <div class="col-12 col-md-6 col-lg-4 q-pa-sm">
          <q-card>
            <q-card-section>
              <div class="text-h6">Memory Usage</div>
              <div :class="`text-subtitle2 ${data.oshiMemoryUsage>=80?'text-negative':null}`">{{data.oshiMemoryUsage || 0}} %</div>
            </q-card-section>
            <q-card-section class="text-h4 row">
              <div>{{`${data.oshiMemoryAvailable} / ${data.oshiMemoryTotal}`}}</div>
            </q-card-section>
            <q-card-section v-if="data.oshiMemoryUsage">
              <q-linear-progress
                :value="data.oshiMemoryUsage/100"
                :color="`${data.oshiMemoryUsage>=80?'negative':null}`"
              />
            </q-card-section>
          </q-card>
          <q-card class="q-mt-md">
            <q-card-section>
              <div class="text-h6">JVM Memory Usage</div>
              <div :class="`text-subtitle2 ${data.jvmMemoryUsage>=80?'text-negative':null}`">{{data.jvmMemoryUsage || 0}} %</div>
            </q-card-section>
            <q-card-section class="text-h4 row">
              <div>{{`${data.jvmMemoryAvailable} / ${data.jvmMemoryTotal}`}}</div>
            </q-card-section>
            <q-card-section v-if="data.jvmMemoryUsage">
              <q-linear-progress
                :value="data.jvmMemoryUsage/100"
                :color="`${data.jvmMemoryUsage>=80?'negative':null}`"
              />
            </q-card-section>
          </q-card>
          <q-card class="
                q-mt-md">
            <q-card-section>
              <div class="text-h6">JVM Heap Memory Usage</div>
            </q-card-section>
            <q-card-section class="text-h4 row">
              <div>{{data.jvmHeapMemoryInit}}</div>
              <q-space />
              <div class="text-caption">Init</div>
            </q-card-section>
            <q-card-section class="text-h4 row">
              <div>{{data.jvmHeapMemoryMax}}</div>
              <q-space />
              <div class="text-caption">Max</div>
            </q-card-section>
            <q-card-section class="text-h4 row">
              <div>{{data.jvmHeapMemoryUsed}}</div>
              <q-space />
              <div class="text-caption">Used</div>
            </q-card-section>
            <q-card-section class="text-h4 row">
              <div>{{data.jvmHeapMemoryCommitted}}</div>
              <q-space />
              <div class="text-caption">Committed</div>
            </q-card-section>
          </q-card>
        </div>
        <div class="col-12 col-md-12 col-lg-4 q-pa-sm">
          <q-card>
            <q-card-section>
              <div class="text-h6">Disk Usage</div>
            </q-card-section>
            <q-card-section class="text-h4">
              <div
                v-for="(disk, index) in data.oshiOsFileSystemDisks"
                :key="index"
                class="q-mb-md"
              >
                <div>{{`${disk.diskAvailable} / ${disk.diskTotal}`}}</div>
                <div :class="`text-weight-bolder text-right text-h6 q-mt-md ${disk.diskUsage>=80?'text-negative':null}`">{{disk.diskUsage}} %</div>
                <div class="text-body1 q-mt-md q-mb-md">{{disk.mount}}</div>
                <div v-if="disk.diskUsage">
                  <q-linear-progress
                    :value="disk.diskUsage/100"
                    :color="`${disk.diskUsage>=80?'negative':null}`"
                  />
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
        <div class="col-12 col-md-6 col-lg-4 q-pa-sm">
          <q-card>
            <q-card-section>
              <div class="text-h6 row">
                <div>Uptime</div>
              </div>
              <div class="text-subtitle2">{{data.javaStartTime}}</div>
            </q-card-section>
            <q-card-section class="text-h4">
              {{data.javaRunTime}}
            </q-card-section>
          </q-card>
        </div>
      </div>
      <q-inner-loading :showing="loading">
        <q-spinner-hourglass
          size="sm"
          color="primary"
        />
      </q-inner-loading>
    </div>
  </q-page>
</template>

<script>
export default {
  name: 'Server',
  meta: { title: '服务器监控' },
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
    async onRequest () {
      this.loading = true
      await this.$fetchData({
        url: '/server/rt',
        method: 'GET'
      }).then(response => {
        const { result, data } = response.data
        if (result && data) {
          this.data = data
        }
      }).catch(error => {
        console.error(error)
      })
      setTimeout(() => {
        this.loading = false
      }, 1000)
    }
  }
}
</script>
<style lang="sass" scoped>
</style>
