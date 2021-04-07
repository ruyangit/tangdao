<template>
  <div>
    <img
      width="100%"
      src="img/vther.jpg"
    />
    <div class="q-pa-md">
      <q-category-modal :treeData="list" />
    </div>
    <!-- <div
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
    </div> -->
  </div>
</template>

<script>
export default {
  name: 'page1',
  data () {
    return {
      list: [
        {
          id: '1',
          label: '系统管理',
          children: [
            {
              id: '1-1',
              label: '组织管理',
              children: [
                { id: '1-1-1', label: '用户管理' },
                { id: '1-1-2', label: '机构管理' },
                { id: '1-1-3', label: '公司管理' },
                { id: '1-1-4', label: '岗位管理' }
              ]
            },
            {
              id: '1-2',
              label: '权限管理',
              children: [
                { id: '1-2-1', label: '角色管理' },
                { id: '1-2-2', label: '系统管理员' },
                { id: '1-2-3', label: '安全审计' }
              ]
            },
            {
              id: '1-3',
              label: '系统设置',
              children: [
                { id: '1-3-1', label: '菜单管理' },
                { id: '1-3-2', label: '资源管理' },
                { id: '1-3-3', label: '参数管理' },
                { id: '1-3-4', label: '字典管理' },
                { id: '1-3-5', label: '行政区划' }
              ]
            },
            {
              id: '1-4',
              label: '系统监控',
              children: [
                { id: '1-4-1', label: '系统日志' },
                { id: '1-4-2', label: '数据监控' },
                { id: '1-4-3', label: '缓存管理' },
                { id: '1-4-4', label: '服务器监控' },
                { id: '1-4-5', label: '作业管理' }
              ]
            }
          ]
        },
        { id: '2', label: '消息管理' },
        { id: '3', label: '流程管理' },
        { id: '4', label: '企业' },
        { id: '5', label: '办公' },
        { id: '6', label: '客户' },
        { id: '7', label: '设置' }
      ]
    }
  }
}
</script>

<style lang="sass" scoped>
</style>
