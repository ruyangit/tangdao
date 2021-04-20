<template>
  <q-page class="my-page">
    <div class="row items-center justify-between">
      <div class="my-page-header q-pb-none">
        <q-breadcrumbs align="left">
          <q-breadcrumbs-el
            label="首页"
            to="/"
          />
          <q-breadcrumbs-el
            label="系统管理"
            to="/sys"
          />
          <q-breadcrumbs-el label="区域列表" />
        </q-breadcrumbs>
      </div>
    </div>

    <div class="my-page-body">
      <div class="my-table ">
        <div class="my-search">
          <div class="q-gutter-md row items-start">
            <div class="text-h6">Area Record</div>
            <q-space />
            <q-btn
              label="展开"
              color="primary"
              @click="onExpand"
            />
            <q-btn
              label="折叠"
              color="primary"
              @click="onCollapse"
            />
            <q-btn
              label="刷新"
              color="primary"
              :loading="loading"
              @click="onRefresh"
            />
          </div>
        </div>
        <q-markup-table>
          <thead>
            <tr>
              <th class="text-left wd-300">AreaName</th>
              <th class="text-left wd-250">AreaNames</th>
              <th class="text-left wd-80">Sort</th>
              <th class="text-left wd-80">Leaf</th>
              <th class="text-left"></th>
              <th class="text-left wd-150">CreateDate</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(item,index) in retData"
              :key="item.id"
              :id="item.id"
              v-show="item.show || true"
            >
              <td class="relative-position">
                <div
                  :style="`width:${item.level?item.level*20:20}px; text-align:right;`"
                  class="relative-position float-left"
                >
                  <q-spinner-oval
                    color="primary"
                    size="10px"
                    style="margin-right: 6px"
                    v-if="item.loading || false"
                  />
                  <template v-else>
                    <div
                      class="item-circle"
                      v-if="item.treeLeaf==='1'"
                    >
                    </div>
                    <q-icon
                      :id="`onc_${item.id}`"
                      :name="item.show?'arrow_drop_down':'arrow_right'"
                      size="20px"
                      color="primary"
                      class="cursor-pointer"
                      @click="expandRow(item, index)"
                      v-else
                    />
                  </template>
                </div>
                <span
                  class="absolute"
                  style="top:10px;"
                >（{{item.id}}）{{item.areaName}}</span>
              </td>
              <td>{{item.treeNames}}</td>
              <td>{{item.treeSort}}</td>
              <td>{{item.treeLeaf}}</td>
              <td>{{item.remarks}}</td>
              <td>{{item.createDate}}</td>
            </tr>
          </tbody>
        </q-markup-table>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
  name: 'AreaList',
  data () {
    return {
      loading: false,
      retData: []
    }
  },
  mounted () {
    this.onRefresh()
  },
  methods: {
    async onRefresh () {
      this.retData = await this.onRequestData({ id: '0' })
    },
    async onRequestData (node) {
      this.$set(node, 'loading', true)
      this.loading = true
      let _data
      await this.$fetchData({
        url: '/area/listData',
        method: 'GET',
        params: { pid: node.id }
      }).then(response => {
        const { result, data } = response.data
        if (result && data) {
          _data = data
        }
      }).catch(error => {
        console.error(error)
      })
      setTimeout(_ => {
        this.$set(node, 'loading', false)
        this.loading = false
      }, 500)
      return _data
    },
    async expandRow (node, index) {
      if (!node.children) {
        const children = await this.onRequestData(node)
        if (children && children.length > 0) {
          if (!node.level) {
            this.$set(node, 'level', 1)
          }
          children.map((item, cindex) => {
            item.level = node.level + 1
            this.retData.splice((index + cindex + 1), 0, item)
          })
          this.$set(node, 'children', children)
          this.$set(node, 'show', true)
        }
      } else {
        this.$set(node, 'show', !node.show)
        const travel = (item) => {
          item.children && item.children.map(ele => {
            if (node.show) {
              if (item.show) {
                document.getElementById(ele.id).style = ''
              }
            } else {
              document.getElementById(ele.id).style = 'display: none'
            }
            if (ele.children && ele.children.length > 0) {
              travel(ele)
            }
          })
        }
        node.children && node.children.forEach(item => {
          document.getElementById(item.id).style = 'display:' + (node.show ? '' : 'none')
          travel(item)
        })
      }
    },
    async onExpand () {
      const temp = []
      const temp_ = []
      this.retData && this.retData.map(item => {
        if (!item.children && item.treeLeaf === '0') {
          // load data
          temp.push({ id: item.id, show: true })
        } else if (item.children && item.show === false) {
          // expand data
          temp_.push({ id: item.id, show: true })
        }
      })
      // 需要展开的数据
      if (temp_.length > 0) {
        this.pprf(temp_)
      } else {
        if (temp.length > 100) {
          console.warn('展开数据过多：' + temp.length)
        } else {
          this.pprf(temp)
        }
      }
    },
    pprf (nodes) {
      let p = Promise.resolve()
      for (let i = 0; i < nodes.length; i++) {
        p = p.then(_ => new Promise(resolve =>
          setTimeout(function () {
            var ele = document.getElementById('onc_' + nodes[i].id)
            if (ele && nodes[i].show) {
              ele.click()
            }
            resolve()
          }, 100)
        ))
      }
    },
    onCollapse () {
      const travel = (data) => {
        data && data.forEach(ele => {
          ele.show = false
          document.getElementById(ele.id).style = 'display: none'
          if (ele.children && ele.children.length > 0) {
            travel(ele.children)
          }
        })
      }
      this.retData && this.retData.forEach(item => {
        item.show = false
        if (item.children && item.children.length > 0) {
          travel(item.children)
        }
      })
    }
  }
}
</script>

<style lang="sass" scoped>
.item-circle
  width: 8px
  height: 8px
  float: right
  border-radius: 100%
  border: 2px solid #bbdefb
  margin-right: 6px
</style>
