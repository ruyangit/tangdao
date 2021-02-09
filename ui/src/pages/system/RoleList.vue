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
            label="权限"
            to="/system"
          />
          <q-breadcrumbs-el label="角色" />
        </q-breadcrumbs>
      </div>
    </div>

    <div class="my-page-body">
      <div class="my-tabs">
        <q-tabs
          narrow-indicator
          align="left"
          class="text-grey"
          active-color="primary"
          indicator-color="primary"
        >
          <q-route-tab to="/system/role">角色列表</q-route-tab>
          <q-route-tab to="/system/role/form">新增角色</q-route-tab>
        </q-tabs>
      </div>
      <div class="my-table">
        <q-table
          :data="data"
          :columns="columns"
          row-key="id"
          :pagination.sync="pagination"
          :loading="loading"
          :filter="roleName"
          @request="onRequest"
          binary-state-sort
          square
        >
          <template v-slot:no-data="{ message }">
            <div class="full-width row flex-center q-gutter-sm q-pa-lg">
              <span>
                {{ message }}
              </span>
            </div>
          </template>

          <template v-slot:body="props">
            <q-tr :props="props">
              <q-td
                key="roleName"
                :props="props"
              >
                <router-link
                  :to="`role/form/${props.row.roleCode}`"
                  class="text-primary"
                >{{ props.row.roleName|| '-' }}</router-link>
              </q-td>
              <q-td
                key="remark"
                :props="props"
                class="text--line2-f"
              >{{ props.row.remarks }}</q-td>
              <q-td
                key="status"
                :props="props"
              >
              </q-td>
              <q-td
                key="createDate"
                :props="props"
              >{{ props.row.createDate }}</q-td>
              <q-td
                key="action"
                :props="props"
                class="q-gutter-xs action"
              >
                <router-link
                  :to="`role/form/${props.row.roleCode}`"
                  class="text-primary"
                >编辑</router-link>
                <a
                  class="text-primary"
                  href="javascript:;"
                  @click="onRolePolicy(props.row)"
                >授权</a>
                <a
                  class="text-primary"
                  href="javascript:;"
                >删除</a>
              </q-td>
            </q-tr>
          </template>
        </q-table>
      </div>
    </div>
    <!-- <role-form
      v-model="fixed"
      v-on:refresh="onRefresh"
    />
    <role-edit
      v-model="fixedEdit"
      v-on:refresh="onRefresh"
      :role="role"
    /> -->
    <!-- <policy-selected
      v-model="fixedPolicyEdit"
      :id="role.id"
      :label="role.roleName"
      :data="rolePolicyList"
      url="/admin/role-policies"
    /> -->
  </q-page>
</template>

<script>
// import RoleForm from './RoleForm.vue'
// import RoleEdit from './RoleEdit.vue'
// import PolicySelected from './PolicySelected.vue'
export default {
  name: 'RoleList',
  components: {
    // RoleForm,
    // RoleEdit,
    // PolicySelected
  },
  data () {
    return {
      loading: false,
      roleName: null,
      pagination: {
        sortBy: null,
        descending: false,
        page: 1,
        rowsPerPage: 10,
        rowsNumber: 10
      },
      columns: [
        { name: 'roleName', label: '角色名称', align: 'left', field: 'roleName', style: 'width: 200px' },
        { name: 'remark', label: '角色描述', align: 'left', field: 'remark' },
        { name: 'status', label: '状态', align: 'center', field: 'status', sortable: true, style: 'width: 100px' },
        { name: 'createDate', label: '创建时间', align: 'center', field: 'createDate', style: 'width: 180px' },
        { name: 'action', label: '操作', field: 'action', align: 'center', style: 'width: 100px' }
      ],
      role: {},
      data: []
    }
  },
  mounted () {
    this.onRefresh()
  },
  methods: {
    onRefresh () {
      this.pagination.page = 0
      this.onRequest({
        pagination: this.pagination,
        filter: null
      })
    },
    async onRequest (props) {
      const { page, rowsPerPage, sortBy, descending } = props.pagination
      const filter = props.filter
      this.loading = true
      await this.$fetchData({
        url: '/v1/system/queryRolePage',
        method: 'GET',
        params: { current: page, size: rowsPerPage, roleName: filter }
      }).then(response => {
        const { code, data } = response.data
        if (code === 0 && data) {
          this.pagination.page = data.current
          this.pagination.rowsNumber = data.total
          this.pagination.rowsPerPage = data.size

          this.pagination.sortBy = sortBy
          this.pagination.descending = descending
          this.data = data.records
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
