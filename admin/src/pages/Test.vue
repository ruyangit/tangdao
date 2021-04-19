<template>
  <div class="q-pa-md">
    <!-- {{retData}} -->
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
            @click="onRefresh"
          />
        </div>
      </div>
      <q-markup-table>
        <!-- <q-linear-progress indeterminate /> -->
        <thead>
          <tr>
            <th class="text-left wd-300">AreaName</th>
            <th class="text-left wd-250">AreaNames</th>
            <th class="text-left wd-80">Sort</th>
            <th class="text-left wd-80">Leaf</th>
            <th class="text-left wd-80">Loading</th>
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
              </div>
              <span
                class="absolute"
                style="top:10px;"
              >（{{item.id}}）{{item.areaName}}</span>
            </td>
            <td>{{item.treeNames}}</td>
            <td>{{item.treeSort}}</td>
            <td>{{item.treeLeaf}}</td>
            <td>
              <q-spinner-oval
                color="primary"
                size="10px"
                v-show="item.loading || false"
              />
            </td>
            <td>{{item.remarks}}</td>
            <td>{{item.createDate}}</td>
          </tr>
        </tbody>
      </q-markup-table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Test',
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
      // this.retData = this.$options.data().data.filter(item => { return item.pid === '0' })
      this.retData = await this.onRequestData({ id: '0' })
    },
    async onRequestData (node) {
      this.$set(node, 'loading', true)
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
      this.$set(node, 'loading', false)
      return _data
      // return this.$options.data().data.filter(item => { return item.pid === node.id })
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
    onExpand () {
      if (this.retData && this.retData.length < 100) {
        const temp = []
        this.retData && this.retData.map((item, index) => {
          if ((item.children && item.show === false) || (!item.children && item.treeLeaf === '0')) {
            temp.push({ id: item.id, show: true, index })
          }
        })
        temp.forEach(item => {
          var ele = document.getElementById('onc_' + item.id)
          if (ele && item.show) {
            console.log(ele)
            // setTimeout(() => {
            //   console.log('onc_' + item.id)
            //   ele.click()
            // }, 1000)
          }
        })
      } else {
        console.warn('展开数据过多：' + this.retData.length)
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
