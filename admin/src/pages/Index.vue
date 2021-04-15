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
            <div class="my-page-header-subtitle">我的工作台</div>
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
      <div class="row q-col-gutter-md">
        <div class="col-12 col-md-6 col-lg-3">
          <q-card flat>
            <q-card-section class="row">
              <div class="col-7">
                <div class="text-h6">3,605</div>
                <div class="text-overline text-primary">Click Through</div>
                <div class="text-body2 text-grey-14 ">No. of clicks to ad that consist of a single impression.</div>
              </div>
              <div class="col-5">
                <div
                  ref="echarts1"
                  style="height:100%;width:100%;"
                ></div>
              </div>
            </q-card-section>
          </q-card>
        </div>
        <div class="col-12 col-md-6 col-lg-3">
          <q-card flat>
            <q-card-section class="row">
              <div class="col-7">
                <div class="text-h6">21,605</div>
                <div class="text-overline text-negative">Click Bind</div>
                <div class="text-body2 text-grey-14 ">No. of clicks to ad that consist of a single impression.</div>
              </div>
              <div class="col-5">
                <div
                  ref="echarts2"
                  style="height:100%;width:100%;"
                ></div>
              </div>
            </q-card-section>
          </q-card>
        </div>
        <div class="col-12 col-md-6 col-lg-3">
          <q-card flat>
            <q-card-section class="row">
              <div class="col-7">
                <div class="text-h6">1,605</div>
                <div class="text-overline text-accent">User Bind</div>
                <div class="text-body2 text-grey-14 ">No. of clicks to ad that consist of a single impression.</div>
              </div>
              <div class="col-5">
                <div
                  ref="echarts3"
                  style="height:100%;width:100%;"
                ></div>
              </div>
            </q-card-section>
          </q-card>
        </div>
        <div class="col-12 col-md-6 col-lg-3">
          <q-card flat>
            <q-card-section class="row">
              <div class="col-7">
                <div class="text-h6">12,605</div>
                <div class="text-overline text-positive">Auth Bind</div>
                <div class="text-body2 text-grey-14 ">No. of clicks to ad that consist of a single impression.</div>
              </div>
              <div class="col-5">
                <div
                  ref="echarts4"
                  style="height:100%;width:100%;"
                ></div>
              </div>
            </q-card-section>
          </q-card>
        </div>
      </div>
      <div class="row q-col-gutter-md q-mt-none">
        <div class="col-12 col-md-8">
          <q-card flat>
            <q-card-section>
              <div class="row no-wrap items-center">
                <div class="col text-subtitle2 ellipsis">
                  收入增涨统计
                </div>
                <div class="col-auto text-grey text-caption row no-wrap items-center">

                </div>
              </div>
            </q-card-section>
            <q-card-section class="row q-pt-none q-pb-none">
              <div class="col-5">
                <div class="text-h6">122,605</div>
                <div class="text-overline text-primary">Auth Bind</div>
                <div class="text-body2 text-grey-14 ">No. of clicks to ad that consist of a single impression.</div>
              </div>
              <div class="col-5 offset-1">
                <div class="text-h6">62,605</div>
                <div class="text-overline text-positive">客户平均增涨</div>
                <div class="text-body2 text-grey-14 ">No. of clicks to ad that consist of a single impression.</div>
              </div>
            </q-card-section>
            <q-card-section>
              <div
                ref="echarts11"
                style="height:230px;width:100%;"
              ></div>
            </q-card-section>
          </q-card>
          <q-card
            flat
            class="q-mt-md"
          >
            <q-card-section class="text-subtitle2">
              进行中的项目
            </q-card-section>
            <q-card-section class="q-pt-none">
              <q-markup-table
                flat
                bordered
              >
                <thead>
                  <tr>
                    <th class="text-center wd-60"></th>
                    <th class="text-left">Dessert (100g serving)</th>
                    <th class="text-right">Calories</th>
                    <th class="text-right">Fat (g)</th>
                    <th class="text-right">Carbs (g)</th>
                    <th class="text-right">Protein (g)</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td class="text-center">
                      <q-icon
                        name="save_alt"
                        size="14px"
                        color="primary"
                      />
                    </td>
                    <td class="text-left">Frozen Yogurt</td>
                    <td class="text-right">159</td>
                    <td class="text-right">6</td>
                    <td class="text-right">24</td>
                    <td class="text-right">
                      <q-linear-progress :value="0.18" />
                    </td>
                  </tr>
                  <tr>
                    <td class="text-center">
                      <q-icon
                        name="save_alt"
                        size="14px"
                        color="primary"
                      />
                    </td>
                    <td class="text-left">Ice cream sandwich</td>
                    <td class="text-right">237</td>
                    <td class="text-right">9</td>
                    <td class="text-right">37</td>
                    <td class="text-right">
                      <q-linear-progress
                        :value="0.12"
                        color="warning"
                      />
                    </td>
                  </tr>
                  <tr>
                    <td class="text-center">
                      <q-icon
                        name="save_alt"
                        size="14px"
                        color="primary"
                      />
                    </td>
                    <td class="text-left">Eclair</td>
                    <td class="text-right">262</td>
                    <td class="text-right">16</td>
                    <td class="text-right">23</td>
                    <td class="text-right">
                      <q-linear-progress
                        :value="0.80"
                        color="secondary"
                      />
                    </td>
                  </tr>
                  <tr>
                    <td class="text-center">
                      <q-icon
                        name="save_alt"
                        size="14px"
                        color="primary"
                      />
                    </td>
                    <td class="text-left">Cupcake</td>
                    <td class="text-right">305</td>
                    <td class="text-right">3.7</td>
                    <td class="text-right">67</td>
                    <td class="text-right">
                      <q-linear-progress
                        :value="0.49"
                        rounded
                        color="accent"
                      />
                    </td>
                  </tr>
                  <tr>
                    <td class="text-center">
                      <q-icon
                        name="save_alt"
                        size="14px"
                        color="primary"
                      />
                    </td>
                    <td class="text-left">Gingerbread</td>
                    <td class="text-right">356</td>
                    <td class="text-right">16</td>
                    <td class="text-right">49</td>
                    <td class="text-right">
                      <q-linear-progress
                        :value="0.36"
                        rounded
                        color="purple"
                        track-color="orange"
                      />
                    </td>
                  </tr>
                </tbody>
              </q-markup-table>
            </q-card-section>
          </q-card>
        </div>
        <div class="col-12 col-md-4">
          <q-card flat>
            <!-- <q-card-section horizontal>
              <q-card-section class="q-pt-xs">
                <div class="text-overline">我的账户</div>
                <div class="text-h5 q-mt-sm q-mb-xs">Title</div>
                <div class="text-caption text-grey">
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                </div>
              </q-card-section>
              <q-card-section class="col-5 flex flex-center">
                <q-img
                  :ratio="1"
                  class="rounded-borders"
                  src="https://cdn.quasar.dev/img/boy-avatar.png"
                />
              </q-card-section>
            </q-card-section>
            <q-separator /> -->
            <q-card-actions>
              <q-btn
                flat
                round
                icon="event"
              />
              <q-btn flat>
                7:30PM
              </q-btn>
              <q-btn
                flat
                color="primary"
              >
                Reserve
              </q-btn>
            </q-card-actions>
          </q-card>
          <q-card
            flat
            class="q-mt-md"
          >
            <q-card-section>
              <div class="row no-wrap items-center">
                <div class="col text-subtitle2 ellipsis">
                  我的资源
                </div>
                <div class="col-auto text-caption text-grey ">
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
            <q-card-section class="row q-col-gutter-sm">
              <div class="col-6 col-md-3 q-pa-sm">用户</div>
              <div class="col-6 col-md-3 q-pa-sm">用户组</div>
              <div class="col-6 col-md-3 q-pa-sm">自定义策略</div>
              <div class="col-6 col-md-3 q-pa-sm">系统角色</div>
            </q-card-section>
          </q-card>
        </div>
      </div>
    </div>
    <q-resize-observer @resize="onResize" />
  </q-page>
</template>

<script>
import axios from 'axios'
// import Highcharts from 'highcharts'
// import { Chart } from 'highcharts-vue'
// import annotationsInit from 'highcharts/modules/annotations'
// annotationsInit(Highcharts)
import * as echarts from 'echarts/core'
import {
  GridComponent
} from 'echarts/components'
import {
  BarChart,
  LineChart,
  PieChart
} from 'echarts/charts'
import {
  CanvasRenderer
} from 'echarts/renderers'
echarts.use(
  [GridComponent, BarChart, LineChart, PieChart, CanvasRenderer]
)
var base = +new Date(1968, 9, 3)
var oneDay = 24 * 3600 * 1000
var date = []
var data = [Math.random() * 300]
for (var i = 1; i < 1000; i++) {
  var now = new Date(base += oneDay)
  date.push([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'))
  data.push(Math.round((Math.random() - 0.5) * 20 + data[i - 1]))
}
var base1 = +new Date(2020, 1, 1)
var data1 = [[base1, Math.random() * 300]]
var data2 = [[base1, Math.random() * 300]]
var data3 = [[base1, Math.random() * 300]]
for (var j = 1; j < 356; j++) {
  var now1 = new Date(base1 += oneDay)
  let temp = Math.round((Math.random() - 0.5) * 20 + data1[j - 1][1])
  if (temp <= 0) {
    temp = Math.round(Math.random() * 20 + data1[j - 1][1])
  }
  data1.push([
    [now1.getFullYear(), now1.getMonth() + 1, now1.getDate()].join('/'),
    temp
  ])
  data2.push([
    [now1.getFullYear(), now1.getMonth() + 1, now1.getDate()].join('/'),
    Math.round((Math.random() - 0.5) * 20 + data1[j - 1][1] + 80)
  ])
  data3.push([
    [now1.getFullYear(), now1.getMonth() + 1, now1.getDate()].join('/'),
    Math.round((Math.random() - 0.5) * 20 + data2[j - 1][1] + 180)
  ])
}
export default {
  name: 'Index',
  meta: { title: '控制台总览' },
  data () {
    return {
      tab: 'ProductAndService',
      timeData: [],
      echartsOpt: {},
      option1: {
        grid: {
          top: 10,
          left: 20,
          right: 0,
          bottom: 10
        },
        xAxis: {
          show: false,
          type: 'category',
          data: ['1', '2', '3', '4', '5', '6', '7', '13', '14', '15', '16', '17']
        },
        yAxis: {
          show: false,
          type: 'value'
        },
        series: [
          {
            type: 'bar',
            stack: 'total',

            data: [0, 0, 1201, 1994, 690, 1330, 820, 1201, 1994, 690, 300, 0],
            itemStyle: {
              color: '#66a4fb'
            }
          },
          {
            type: 'bar',
            stack: 'total',
            data: [1920, 1430, 1560, 1380, 1990, 1230, 1210, 1430, 1560, 1380, 1990, 1230, 1210],
            itemStyle: {
              color: '#e5e9f2'
            }
          }
        ],
        animation: false
      },
      option2: {
        grid: {
          top: 10,
          left: 20,
          right: 0,
          bottom: 10
        },
        xAxis: {
          show: false,
          type: 'category',
          data: date
        },
        yAxis: {
          show: false,
          type: 'value'
        },
        series: [
          {
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            itemStyle: {
              color: 'rgb(255, 70, 131)'
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgb(255, 158, 68)'
              }, {
                offset: 1,
                color: 'rgb(255, 70, 131)'
              }])
            },
            data: data
          }
        ],
        animation: false
      },
      option3: {
        grid: {
          top: 10,
          left: 20,
          right: 0,
          bottom: 10
        },
        xAxis: {
          type: 'category',
          show: false
        },
        yAxis: {
          type: 'value',
          show: false
        },
        series: [{
          data: [10, 50, 36, 85, 98, 72, 79, 88, 80],
          type: 'line',
          smooth: true,
          show: false,
          symbol: 'none',
          animationDuration: 1000,
          lineStyle: {
            color: '#9C27B0'
          }
        }],
        animation: false
      },
      option4: {
        grid: {
          top: 10,
          left: 20,
          right: 0,
          bottom: 10
        },
        legend: {
          top: '5%',
          left: 'center'
        },
        series: [
          {
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 3,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 1048 },
              { value: 735 },
              { value: 580 },
              { value: 484 },
              { value: 300 }
            ]
          }
        ],
        animation: false
      },
      option11: {
        grid: {
          top: '0',
          left: '0',
          right: '0',
          bottom: '0'
        },
        xAxis: {
          show: false,
          type: 'time'
        },
        yAxis: {
          show: false,
          type: 'value'
        },
        series: [
          {
            type: 'line',
            smooth: true,
            symbol: 'none',
            lineStyle: {
              width: 0
            },
            itemStyle: {
              color: '#d1e6fa'
            },
            areaStyle: {
              color: '#66a4fb'
            },
            data: data1
          },
          {
            type: 'line',
            smooth: true,
            symbol: 'none',
            lineStyle: {
              width: 0
            },
            areaStyle: {},
            itemStyle: {
              color: '#66a4fb'
            },
            data: data2
          },
          {
            type: 'line',
            smooth: true,
            symbol: 'none',
            itemStyle: {
              color: '#d1e6fa'
            },
            data: data3
          }
        ]
      },
      animation: false
    }
  },
  components: {
  },
  computed: {
    elevationOptions () {
      return null
    }
  },
  mounted () {
    const chart1 = echarts.init(this.$refs.echarts1)
    chart1.setOption(this.option1)
    const chart2 = echarts.init(this.$refs.echarts2)
    chart2.setOption(this.option2)
    const chart3 = echarts.init(this.$refs.echarts3)
    chart3.setOption(this.option3)
    const chart4 = echarts.init(this.$refs.echarts4)
    chart4.setOption(this.option4)
    const chart11 = echarts.init(this.$refs.echarts11)
    chart11.setOption(this.option11)
    chart11.showLoading()
    setTimeout(function () {
      chart11.hideLoading()
    }, 1500)
    // window.onresize = () => {
    //   chart1.resize()
    //   chart2.resize()
    //   chart3.resize()
    //   chart4.resize()
    //   chart11.resize()
    // }

    this.echartsOpt = {
      chart1, chart2, chart3, chart4, chart11
    }

    this.onDataInit()
  },
  destroyed () {
    window.onresize = null
  },
  methods: {
    async onDataInit () {
      this.$store.commit('basic/globalLoadingMutAction', true)
      await axios.get('json/timeData.json').then(response => {
        const { status, data } = response
        if (status === 200) {
          this.timeData = data
        }
      })
      this.$store.commit('basic/globalLoadingMutAction', false)
    },
    onResize () {
      if (this.echartsOpt && this.echartsOpt.chart1) {
        this.echartsOpt.chart1.resize()
        this.echartsOpt.chart2.resize()
        this.echartsOpt.chart3.resize()
        this.echartsOpt.chart4.resize()
        this.echartsOpt.chart11.resize()
      }
    }
  }
}
</script>
<style lang="sass" scoped>
#container
  width: 100%
</style>
