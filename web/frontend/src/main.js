// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import VueBreadcrumbs from 'vue-breadcrumbs'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.min.css'

Vue.config.productionTip = false
Vue.use(BootstrapVue)
Vue.use(VueBreadcrumbs, {
  template: '<nav class="breadcrumb" v-if="$breadcrumbs.length"> ' +
  '<router-link class="breadcrumb-item" v-for="(crumb, key) in $breadcrumbs" :to="linkProp(crumb)" :key="key">{{ crumb | crumbText }}</router-link> ' +
  '</nav>'
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: {App},
  template: '<App/>'
})
