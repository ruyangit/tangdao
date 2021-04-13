<template>
  <q-page class="my-page">
    <div class="row items-center justify-between">
      <div class="my-page-header">
        <q-breadcrumbs align="left">
          <q-breadcrumbs-el
            label="首页"
            to="/"
          />
          <q-breadcrumbs-el label="控制台总览" />
        </q-breadcrumbs>
        <div class="row">
          <div>
            <div class="my-page-header-subtitle">产品与服务</div>
            <!-- <div class="q-mt-sm text-caption">您好, admin</div> -->
          </div>
          <q-space />
          <div class="row wrap content-end">
            您好, system
          </div>
        </div>
      </div>
    </div>
    <div class="my-page-body">
      <q-banner
        dense
        inline-actions
        class="q-mb-md bg-amber-1"
      >
        关于系统TabLine上线，邀请你参与问卷调查。
        <template v-slot:action>
          <q-btn
            flat
            label="不参与"
          />
          <q-btn
            flat
            label="前往填写"
            color="primary"
          />
        </template>
      </q-banner>
      <div class="row">
        <div class="col col-md-6 col-lg-3">
          <q-card flat>
            <q-card-section class="row">
              <div class="col-7">
                <div class="text-h6">3,605</div>
                <div class="text-overline text-primary">Click Through</div>
                <div class="text-body2 text-grey-14 ">No. of clicks to ad that consist of a single impression.</div>
              </div>
              <div class="col-5">
                <div class="chart-ten">
                  <div
                    id="flotChart3"
                    class="flot-chart"
                  ></div>
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="row q-col-gutter-md q-mt-md">
        <div class="col-12 col-md-8">
          <QHighcharts
            :options="elevationOptions"
            id="container"
          />
        </div>
        <div class="col-12 col-md-4">
          <q-card flat>
            <q-card-section>
              <div class="row no-wrap items-center">
                <div class="col text-subtitle2 ellipsis">
                  我的资源
                </div>
                <div class="col-auto text-grey text-caption row no-wrap items-center">
                  总量（128G）
                </div>
              </div>
            </q-card-section>

            <q-card-section class="q-pt-none">
              <q-linear-progress
                size="18px"
                :value="0.2"
                color="primary"
              >
                <div class="absolute-full flex flex-center">
                  <q-badge
                    color="white"
                    text-color="primary"
                    label="20%"
                  />
                </div>
              </q-linear-progress>
            </q-card-section>
          </q-card>
          <q-card
            flat
            class="q-mt-md"
          >
            <q-card-section class="text-subtitle2">
              权限管理
            </q-card-section>
            <q-card-section class="row">
              <div class="col-3 q-pa-sm">用户</div>
              <div class="col-3 q-pa-sm">用户组</div>
              <div class="col-3 q-pa-sm">自定义策略</div>
              <div class="col-3 q-pa-sm">系统角色</div>
            </q-card-section>
          </q-card>
        </div>
      </div>
    </div>
  </q-page>
</template>

<script>
import axios from 'axios'
import Highcharts from 'highcharts'
import { Chart } from 'highcharts-vue'
import annotationsInit from 'highcharts/modules/annotations'
annotationsInit(Highcharts)

export default {
  name: 'Index',
  meta: { title: '控制台总览' },
  data () {
    return {
      tab: 'ProductAndService',
      elevationData: []
    }
  },
  components: {
    QHighcharts: Chart
  },
  computed: {
    elevationOptions () {
      return {
        chart: {
          type: 'area',
          zoomType: 'x',
          panning: true,
          panKey: 'shift'
        },
        title: {
          text: '2017年环法德法国第八站：Dole站'
        },
        annotations: [{
          labelOptions: {
            backgroundColor: 'rgba(255,255,255,0.5)',
            verticalAlign: 'top',
            y: 15
          },
          labels: [{
            point: {
              xAxis: 0,
              yAxis: 0,
              x: 27.98,
              y: 255
            },
            text: 'Arbois'
          }, {
            point: {
              xAxis: 0,
              yAxis: 0,
              x: 45.5,
              y: 611
            },
            text: 'Montrond'
          }, {
            point: {
              xAxis: 0,
              yAxis: 0,
              x: 63,
              y: 651
            },
            text: 'Mont-sur-Monnet'
          }, {
            point: {
              xAxis: 0,
              yAxis: 0,
              x: 84,
              y: 789
            },
            x: -10,
            text: 'Bonlieu'
          }, {
            point: {
              xAxis: 0,
              yAxis: 0,
              x: 129.5,
              y: 382
            },
            text: 'Chassal'
          }, {
            point: {
              xAxis: 0,
              yAxis: 0,
              x: 159,
              y: 443
            },
            text: 'Saint-Claude'
          }]
        }, {
          labels: [{
            point: {
              xAxis: 0,
              yAxis: 0,
              x: 101.44,
              y: 1026
            },
            x: -30,
            text: 'Col de la Joux'
          }, {
            point: {
              xAxis: 0,
              yAxis: 0,
              x: 138.5,
              y: 748
            },
            text: 'Côte de Viry'
          }, {
            point: {
              xAxis: 0,
              yAxis: 0,
              x: 176.4,
              y: 1202
            },
            text: 'Montée de la Combe<br>de Laisia Les Molunes'
          }]
        }, {
          labelOptions: {
            shape: 'connector',
            align: 'right',
            justify: false,
            crop: true,
            style: {
              fontSize: '0.8em',
              textOutline: '1px white'
            }
          },
          labels: [{
            point: {
              xAxis: 0,
              yAxis: 0,
              x: 96.2,
              y: 783
            },
            text: '6.1 km climb<br>4.6% on avg.'
          }, {
            point: {
              xAxis: 0,
              yAxis: 0,
              x: 134.5,
              y: 540
            },
            text: '7.6 km climb<br>5.2% on avg.'
          }, {
            point: {
              xAxis: 0,
              yAxis: 0,
              x: 172.2,
              y: 925
            },
            text: '11.7 km climb<br>6.4% on avg.'
          }]
        }],
        xAxis: {
          labels: {
            format: '{value} km'
          },
          minRange: 5,
          title: {
            text: '距离'
          }
        },
        yAxis: {
          startOnTick: true,
          endOnTick: false,
          maxPadding: 0.35,
          title: {
            text: null
          },
          labels: {
            format: '{value} m'
          }
        },
        tooltip: {
          headerFormat: '距离: {point.x:.1f} km<br>',
          pointFormat: '海拔：{point.y} m ',
          shared: true
        },
        legend: {
          enabled: false
        },
        series: [{
          data: this.elevationData,
          lineColor: Highcharts.getOptions().colors[1],
          color: Highcharts.getOptions().colors[2],
          fillOpacity: 0.5,
          name: '海拔',
          marker: {
            enabled: false
          },
          threshold: null
        }]
      }
    }
  },
  mounted () {
    this.elevationDataInit()
  },
  methods: {
    elevationDataInit () {
      this.$store.commit('basic/globalLoadingMutAction', true)
      axios.get('json/elevation.json').then(response => {
        const { status, data } = response
        if (status === 200) {
          this.elevationData = data
        }
      }).finally(e => {
        this.$store.commit('basic/globalLoadingMutAction', false)
      })
    }
  }
}
</script>
<style lang="sass" scoped>
#container
  width: 100%
</style>
