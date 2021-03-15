<template>
  <q-page class="row items-center justify-center">
    <q-card
      class="login-wrapper shadow-24"
      bordered
    >
      <q-card-section horizontal>
        <div class="col-6 q-pl-xs">
          <q-lottie-web :path="'json/coding.json'"></q-lottie-web>
        </div>
        <q-card-section :class="`${$q.screen.gt.xs?'col-6':'col'} q-mt-xs q-pa-xl`">
          <div class="text-h5 text-primary q-mb-xs">用户登录</div>
          <div class="text-h6 q-mt-sm q-mb-xs">欢迎回来，请登录后继续</div>
          <q-form
            @submit="onSubmit"
            class="login-form my-form q-mt-lg"
          >
            <q-input
              outlined
              dense
              no-error-icon
              v-model.trim="form.username"
              placeholder="账号"
              :rules="[ val => val && val.length > 0 || '请输入用户账号']"
            />
            <q-input
              outlined
              dense
              no-error-icon
              type="password"
              v-model.trim="form.password"
              placeholder="密码"
              :rules="[ val => val && val.length > 0 || '请输入登录密码']"
            />
            <div class="column q-gutter-y-md q-mt-none">
              <q-checkbox
                v-model="form.rememberMe"
                label="记住账号"
                dense
              />
            </div>
            <div class="q-mt-md column items-center">
              <q-btn
                label="登录"
                type="submit"
                color="primary"
                class="full-width"
              />
              <div class="q-mt-md">如果还没有账号？<router-link
                  to="/user/register"
                  class="text-primary"
                >点击注册</router-link>
              </div>
            </div>
          </q-form>

        </q-card-section>
      </q-card-section>
      <q-separator />

      <q-card-actions v-if="$q.screen.gt.sm">
        <div class="q-pl-xs">
          <q-brand-color />
        </div>
        <q-space />
        <q-btn
          flat
          round
          icon="settings_system_daydream"
        />
      </q-card-actions>
      <q-inner-loading :showing="loading">
        <q-spinner-hourglass
          size="sm"
          color="primary"
        />
      </q-inner-loading>
    </q-card>
  </q-page>
</template>

<script>
export default {
  name: 'UserLogin',
  components: {
    QBrandColor: () => import('components/BrandColor/BrandColor'),
    QLottieWeb: () => import('components/LottieWeb/LottieWeb')
  },
  data () {
    return {
      loading: false,
      form: {
        username: '',
        password: '',
        rememberMe: false
      }
    }
  },

  methods: {
    onSubmit () {
      this.loading = true
      this.$store.dispatch('session/loginAction', this.form).then(e => {
        const params = { redirect: this.$route.query.redirect || '/' }
        this.$router.push({ path: params.redirect })
      }).finally(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style lang="sass" scoped>
.login-wrapper, login-form
  width: 840px
@media (max-width: 1023px)
  .login-wrapper, login-form
    width: 98%
@media (max-width: 599px)
  .q-pa-xl
    padding: 15px
</style>
